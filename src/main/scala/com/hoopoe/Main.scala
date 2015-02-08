package com.hoopoe

import java.net.InetSocketAddress
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.io.IO
import akka.io.Tcp
import akka.io.Tcp.Bind
import akka.io.Tcp.Bound
import akka.io.Tcp.Connected
import akka.io.Tcp.PeerClosed
import akka.io.Tcp.Received
import akka.io.Tcp.Register
import akka.io.Tcp.Write
import akka.util.ByteString
import com.hoopoe.message.Message
import akka.actor.Cancellable
import scala.concurrent.duration.DurationInt

object Main extends App {
  val system = ActorSystem("hoopoe")
  
  system.actorOf(Props[TcpActor], "tcp")
}

class TcpActor extends Actor with ActorLogging {
  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(1234))

  def receive = {
    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[TcpHandlerActor])
      val connection = sender
      connection ! Register(handler)

    case Bound(port) => None
    case m @ _ => println("Unhandled: " + m)
  }
}

class TcpHandlerActor extends Actor with ActorLogging {
  import Tcp._
  import context.dispatcher

  var schedule = context.system.scheduler.scheduleOnce(10.seconds, self, Close)

  def receive = {
    case Received(data) =>
      schedule.cancel
      val d = ascii(data)
//      identifyMessage(d)
      log.info(d)
      schedule = context.system.scheduler.scheduleOnce(10.seconds, sender, Close)
      //sender ! Write(ByteString(d + "\n"))

    case PeerClosed =>
      schedule.cancel
      context stop self

    case _ => None
  }
  
  def identifyMessage(data: String) = {
    val m = Message(data)
    println(m)
  }

  def ascii(bytes: ByteString): String = bytes.decodeString("US-ASCII")
}
