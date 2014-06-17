name := """UniverseSiteMonitor"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" % "mongo-java-driver" % "2.7.3",
  "org.mongojack" % "play-mongojack_2.10" % "2.0.0-RC2",
  "net.sf.oval" % "oval" % "1.84",
  "org.apache.commons" % "commons-email" % "1.3.2"
)

resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/"
