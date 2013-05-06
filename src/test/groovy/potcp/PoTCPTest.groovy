package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import groovy.util.GroovyTestCase
import org.junit.Test

import java.lang.Runnable

class EchoHandler extends Handler {
  String getMethod() { return "echo" }
  String getFormat() { return "text" }
  Response call(Request req) {
    Response res = req.makeResponse()
    res.data = req.data
    return res
  }
}

class ServerRunner implements Runnable {
  Server server
  ServerRunner(Server server) { this.server = server }
  
  void run() {
    server.bind(9000)
  }
}

class PoTCPTest extends GroovyTestCase {
  Server server
  Thread serverThread
  
  PoTCPTest() {
    this.server = new Server()
    this.server.register(new EchoHandler())
  }
  
  @Test
  void testEchoServer() {
    System.out.println("Server starting")
    // this.server.bind(9000)
    this.serverThread = new Thread(new ServerRunner(this.server))
    this.serverThread.start()
    System.out.println("Server listening")
  }
}
