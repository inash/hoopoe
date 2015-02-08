package com.hoopoe.extension

import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.immutable.Map

import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig

import akka.actor.ActorSystem
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider

class DatabaseImpl(val system: ActorSystem) extends Extension {
  import scala.collection.JavaConversions._

  /** Get the glow database configuration object. */
  val databases = system.settings.config.getObject("hoopoe.database").filter { c =>
    val config = c._2.atKey(c._1).getConfig(c._1)
    if (config.hasPath("ignore") && config.getBoolean("ignore") == true) false else true
  }.toMap

  /** Map the configurations and create connection pools by name. */
  val pools: Map[String, BoneCP] = databases.map { case (key, value) =>
    system.log.info(s"creating pool to $key")

    val db = value.atKey(key).getConfig(key)

    /** Throw runtime expection if the squeryl adapter is not specified. */
    if (db.hasPath("adapter") == false)
      throw new RuntimeException(s"Squeryl adapter needs to be specified for key $key")

    /** Reference the JDBC driver class. */
    Class.forName(db.getString("driver-class"))

    /** Initialize the bonecp configuration. */
    val bcConfig: BoneCPConfig = new BoneCPConfig
    bcConfig.setJdbcUrl(db.getString("connection-string"))
    
    /** Set username if provided in the configuration. */
    if (db.hasPath("user")) bcConfig.setUsername(db.getString("user"))

    /** Set password if provided in the configuration. */
    if (db.hasPath("pass")) bcConfig.setPassword(db.getString("pass"))

    bcConfig.setMinConnectionsPerPartition(db.getInt("partition.minConnections"))
    bcConfig.setMaxConnectionsPerPartition(db.getInt("partition.maxConnections"))
    bcConfig.setPartitionCount(db.getInt("partition.count"))

    /** Initialize the connection pool and return it. */
    key -> new BoneCP(bcConfig)
  }

  def getConnection(key: String) = {
    pools(key).getConnection
  }
}

object Database extends ExtensionId[DatabaseImpl] with ExtensionIdProvider {

  /** Define required method to lookup this extension. */
  override def lookup = Database

  /** Define required method to create this extension. */
  override def createExtension(system: ExtendedActorSystem) = {
    new DatabaseImpl(system)
  }

  /** Define required method to get a reference to this extension. */
  override def get(system: ActorSystem): DatabaseImpl = super.get(system)
}
