package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

import java.lang.Runnable
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService

import java.io.InputStream
import java.io.OutputStream

import java.util.Map
import java.util.concurrent.ConcurrentHashMap

import groovy.transform.CompileStatic

abstract class Handler {
  abstract String getMethod()
  abstract String getFormat()
  abstract Response call(Request req)
}

class Server {
  private ExecutorService pool
  private ServerSocket server_socket
  private Map<String, Handler> handlers
  
  Server() {
    this.server_socket = null
    this.pool          = null
    this.handlers      = new ConcurrentHashMap<String, Handler>()
  }
  
  @CompileStatic
  Handler getHandler(String signature) {
    return this.handlers.get(signature)
  }
  
  @CompileStatic
  void register(Handler handler) {
    String signature = handler.getMethod()+"."+handler.getFormat()
    this.handlers.put(signature, handler)
  }
  
  @CompileStatic
  void bind(int port) {
    this.pool = Executors.newCachedThreadPool()
    this.server_socket = new ServerSocket(port)
    
    for(;;) {
      this.pool.execute(new SocketHandler(this, this.server_socket.accept()))
    }
  }
  
  @CompileStatic
  static Response createMethodNotFoundResponse(Request req) {
    Response rep = new Response()
    rep.status = 404
    rep.format = "text"
    String data_string = "Method ${req.method}.${req.format} not found"
    rep.data = data_string as byte[]
    return rep
  }
}

class SocketHandler implements Runnable {
  Server server
  Socket socket
  
  SocketHandler(Server server, Socket socket) {
    this.server = server
    this.socket = socket
  }
  
  @CompileStatic
  void run() {
    InputStream  input = this.socket.getInputStream()
    OutputStream output = this.socket.getOutputStream()
    
    Request req = Coder.decodeRequest(input)
    
    Response rep
    Handler handler = this.server.getHandler(req.method+"."+req.format)
    if(handler == null) {
      rep = this.server.createMethodNotFoundResponse(req)
    } else {
      rep = handler.call(req)
    }
    
    Coder.encodeResponse(rep, output)
    this.socket.close()
  }
}
