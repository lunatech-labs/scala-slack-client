name := "scala-slack-client"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.0-M2"

libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-xml" % "2.0.0-M2"
libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.0-M2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
