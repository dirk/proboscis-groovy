package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import groovy.transform.CompileStatic

import java.net.Socket

class Client {
  Socket socket
  InputStream  input
  OutputStream output
  
  @CompileStatic
  Client(String host, int port) {
    this.socket = new Socket(host, port)
    this.input  = this.socket.getInputStream()
    this.output = this.socket.getOutputStream()
  }
  
  @CompileStatic
  void close() {
    this.socket.close()
  }
  
  @CompileStatic
  Response call(Request req) {
    Coder.encodeRequest(req, this.output)
    return Coder.decodeResponse(this.input)
  }
  
  @CompileStatic
  Response call(String method, String format, String data) {
    Request req = new Request()
    req.method = method
    req.format = format
    req.data = data as byte[]
    return this.call(req)
  }
}
