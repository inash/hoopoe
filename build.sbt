name := """hoopoe"""

version := "1.0"

organization := "com.hoopoe"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

unmanagedSourceDirectories in Compile <++= baseDirectory { base =>
  Seq(
    base / "src/main/resources"
  )
}

Revolver.settings

mainClass in Revolver.reStart := Some("com.hoopoe.Main")
