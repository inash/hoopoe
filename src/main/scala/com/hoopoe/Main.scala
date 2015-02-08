package com.hoopoe

import java.net.InetSocketAddress

import com.hoopoe.message.MG
import com.hoopoe.message.Message
import com.hoopoe.message.iTG

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.io.IO
import akka.io.Tcp
import akka.io.Tcp.Bind
import akka.io.Tcp.Bound
import akka.io.Tcp.Connected
import akka.io.Tcp.Received
import akka.io.Tcp.Register
import akka.util.ByteString

object Main extends App {
  val system = ActorSystem("hoopoe")
  
  system.actorOf(Props[TcpActor], "tcp")
}

class TcpActor extends Actor {
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

class TcpHandlerActor extends Actor {
  import Tcp._
  import context.dispatcher

  def receive = {
    case Received(data) =>
      val d = ascii(data)
      identifyMessage(d)

    case _ => None
  }

  def identifyMessage(data: String) = {
    Message(data) match {
      case m: MG  =>
      case m: iTG =>
        val sql =
          "INSERT INTO messages (protocol, device_imei, device_name, device_status1, " +
            "device_status2, device_status3, device_analog1, device_analog2, " +
            "device_battery, gprmc_time, gprmc_date, gsm_rssi) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        val conn = getDatabaseConnection("default")
        val stmt = conn.prepareStatement(sql)
        stmt.setString(1,  "iTG")          // protocol
        stmt.setString(2,  m.imei)         // device_imei
        stmt.setString(3,  m.tid)          // device_name
        stmt.setString(4,  m.event)        // device_status1
        stmt.setString(5,  m.tevent)       // device_status2
        stmt.setString(6,  m.reportcount)  // device_status3
        stmt.setString(7,  m.tsignalrssi)  // device_analog1
        stmt.setString(8,  m.tpower)       // device_analog2
        stmt.setString(9,  m.battery)      // device_battery
        stmt.setString(10, m.utctime)      // gprmc_time
        stmt.setString(11, m.date)         // gprmc_date
        stmt.setString(12, m.gsmrssi)      // gsm_rssi
  
        stmt.executeUpdate()
        stmt.close()
        conn.close()
    }
  }

  def ascii(bytes: ByteString): String = bytes.decodeString("US-ASCII")
}
