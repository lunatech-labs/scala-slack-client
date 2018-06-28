name := "scala-slack-client"
organization := "com.lunatech"
scalaVersion := "2.12.6"


libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.0-M2",
  "com.typesafe.play" %% "play-ws-standalone-xml" % "2.0.0-M2",
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.0-M2",
//  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

)

