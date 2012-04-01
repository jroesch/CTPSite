import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName         = "Words"
  val appVersion      = "1.0-SNAPSHOT"

  val novusRels = "repo.novus rels" at "http://repo.novus.com/releases/"
  val novusSnaps = "repo.novus snaps" at "http://repo.novus.com/snapshots/"

  val markdown = "Christophs Maven Repo" at "http://maven.henkelmann.eu/"

  val salat = "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"
  val actuarius = "eu.henkelmann" %% "actuarius" % "0.2.3"

  val appDependencies = Seq(
    salat,
    actuarius
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    resolvers ++= Seq(
      novusRels,
      novusSnaps,
      markdown
    )
  )
}
