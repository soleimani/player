import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "player"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.21",
    "com.google.inject" % "guice" % "3.0",
    "org.mockito" % "mockito-all" % "1.9.0",
    "org.fusesource.scalate" %% "scalate-core" % "1.6.1")

  val main = play.Project(appName, appVersion, appDependencies).settings(defaultScalaSettings: _*).settings()
  
}
