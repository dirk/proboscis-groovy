package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import groovy.util.GroovyTestCase

import org.junit.Test
import org.junit.Assert

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
    server.bind(PoTCPTest.PORT)
  }
}

class PoTCPTest extends GroovyTestCase {
  Server server
  Thread serverThread
  
  public static PORT = 9000
  
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
    
    Client client = new Client("localhost", PoTCPTest.PORT)
    
    System.out.println("Client created; calling")
    
    String test_string = "Hello world!"
    Response rep = client.call("echo", "text", test_string)
    Assert.assertEquals(
      "request and response data must be the same",
      new String(rep.data), test_string
    )
    
  }
}
