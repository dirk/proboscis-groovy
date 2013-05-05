package co.langzeit.proboscis.potcp

import java.net.InetSocketAddress
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService

import org.jboss.netty.channel.ChannelFactory
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.bootstrap.ServerBootstrap

import java.net.ServerSocket

class Server {
  /*
  private ChannelFactory factory
  private ServerBootstrap bootstrap
  
  Server() {
    this.factory = new NioServerSocketChannelFactory(
      Executors.newCachedThreadPool(), Executors.newCachedThreadPool()
    )
    this.bootstrap = new ServerBootstrap(factory)
    this.bootstrap.setOption("child.tcpNoDelay", true)
    this.bootstrap.setOption("child.keepAlive",  true)
  }
  
  void bind(InetSocketAddress addr) {
    this.bootstrap.bind(addr)
    System.out.println("Listening...")
  }
  */
  
  private ExecutorService executors
  
  private ServerSocket server_socket
  
  Server() {
    this.server_socket = null
    this.executors = null
  }
  
  void bind(int port) {
    this.executors = Executors.newCachedThreadPool()
  }
  
  
  
  
  
}

