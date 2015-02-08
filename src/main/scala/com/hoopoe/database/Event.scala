package com.hoopoe.database

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

class Event(val adf: String,
			val asdf: String) {
  def this() = this("one", "two")
}
