package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import java.io.InputStream
import java.io.OutputStream
import java.util.Scanner

import groovy.transform.CompileStatic

class Coder {
  @CompileStatic
  static Request decodeRequest(InputStream input) {
    Request req = new Request()
    Scanner s   = new Scanner(input)
    
    s.useDelimiter(".")
    req.method = s.next()
    
    s.useDelimiter(":")
    req.format = s.next()
    
    String length = s.next()
    req.length = length.toInteger()
    
    input.read(req.data, 0, req.length)
    
    return req
  }
}
