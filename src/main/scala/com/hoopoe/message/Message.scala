package com.hoopoe.message

import com.hoopoe.exception.InvalidMessageException

trait Message

object Message {
  def apply(data: String): Message = {
    if (data.substring(0, 1) == "$") MG(data)
    else data.split(",")(0) match {
      case "iTG" => iTG(data)
      case _ => Unknown(data)
    }
  }
}

case class Unknown(data: String) extends Message
