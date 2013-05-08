package co.langzeit.proboscis.potcp
import co.langzeit.proboscis.Request
import co.langzeit.proboscis.Response

import java.io.InputStream
import java.io.OutputStream
import java.util.Arrays

import groovy.transform.CompileStatic

class Coder {
  static byte[] COLON  = ":" as byte[]
  static byte[] PERIOD = "." as byte[]
  // Maximum buffer size for `static readUntil`
  static int BUFFER = 256
  
  @CompileStatic
  static String readUntil(InputStream i, byte delim) {
    byte[] buff = new byte[this.BUFFER]
    byte b
    int c = 0
    for(; c < (this.BUFFER - 1); c++) {
      b = (byte)i.read()
      if(b == delim) { break; }
      buff[c] = b
    }
    // Create a byte array of the exact right length
    byte[] byte_array = Arrays.copyOf(buff, c)
    // Create a new string from the byte array and return it
    return new String(byte_array)
  }
  @CompileStatic
  static String readUntil(InputStream i, byte[] delim) {
    return this.readUntil(i, delim[0])
  }
  
  @CompileStatic
  static Response decodeResponse(InputStream i) {
    Response rep = new Response()
    
    String status = this.readUntil(i, this.COLON)
    rep.status = status.toInteger()
    
    rep.format = this.readUntil(i, this.COLON)
    
    String length = this.readUntil(i, this.COLON)
    rep.length = length.toInteger()
    
    rep.data = new byte[rep.length]
    i.read(rep.data, 0, rep.length)
    
    return rep
  }
  
  
  @CompileStatic
  static void encodeRequest(Request req, OutputStream o) {
    o.write(req.method as byte[])
    o.write(PERIOD)
    o.write(req.format as byte[])
    o.write(COLON)
    o.write(req.data.size().toString() as byte[])
    o.write(COLON)
    o.write(req.data)
  }
  
  @CompileStatic
  static Request decodeRequest(InputStream i) {
    Request req = new Request()
    
    req.method = this.readUntil(i, this.PERIOD)
    
    req.format = this.readUntil(i, this.COLON)
    
    String length = this.readUntil(i, this.COLON)
    req.length = length.toInteger()
    
    req.data = new byte[req.length]
    i.read(req.data, 0, req.length)
    
    // System.out.println("2: ${new String(req.data)}")
    
    return req
  }
  
  @CompileStatic
  static void encodeResponse(Response rep, OutputStream o) {
    o.write(rep.status.toString() as byte[])
    o.write(COLON)
    o.write(rep.format as byte[])
    o.write(COLON)
    o.write(rep.data.size().toString() as byte[])
    o.write(COLON)
    o.write(rep.data)
  }
}
