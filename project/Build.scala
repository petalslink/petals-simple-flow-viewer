import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "petals-simple-flow-viewer"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.ow2.petals" % "petals-log-api" % "1.0.0-SNAPSHOT",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Local Maven Repository" at "file:/D:/InstallationLogiciels/Maven/repository"
  )

}
