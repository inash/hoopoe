package com.hoopoe

import com.hoopoe.extension.Database

import akka.actor.ActorLogging

/** 
 * Abstract actor that extends akka actor with actor logging and implements
 * the glow actor system extensions such as the database extension.
 */
trait Actor extends akka.actor.Actor with ActorLogging { self: akka.actor.Actor =>
  /** Get database session from the database extension for the provided key. */
  def getDatabaseConnection(key: String) = Database(context.system).getConnection(key)
}
