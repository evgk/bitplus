name := """bitplus"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "jp.t2v" %% "play2-auth"      % "0.13.0",
  "jp.t2v" %% "play2-auth-test" % "0.13.0" % "test",
  "mysql" % "mysql-connector-java" % "5.1.27",
  "com.typesafe.play" %% "play-mailer" % "2.4.0",
  "com.typesafe.play" %% "play-ws" % "2.3.1",
   "org.scalaj" %% "scalaj-http" % "1.1.4",
   "net.glxn" % "qrgen" % "1.4",
   "org.clapper" %% "grizzled-scala" % "1.3"
)
