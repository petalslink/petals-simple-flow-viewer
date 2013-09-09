import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "petals-simple-flow-viewer"
  val appVersion      = "1.0-SNAPSHOT"
  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaEbean
  )
  
  val main = play.Project(appName, appVersion, appDependencies)
}
