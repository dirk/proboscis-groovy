package co.langzeit.proboscis

public class Request {
  String method
  String format
  int    length
  byte[] data
  
  Request() {
    this.method = ""
    this.format = ""
    this.length = 0
    this.data   = []
  }
  
  Response makeResponse() {
    Response r = new Response()
    r.status = 200
    r.format = this.format
    return r
  }
}

public class Response {
  int    status
  String format
  int    length
  byte[] data
  
  Response() {
    this.status = 0
    this.format = ""
    this.length = 0
    this.data   = []
  }
}

