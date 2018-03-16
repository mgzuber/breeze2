organization in ThisBuild := "com.mgzuber"
version in ThisBuild := "0.0.1-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.4"
scalacOptions in ThisBuild ++= "-deprecation" ::
                               "-encoding" :: "UTF-8" ::
                               "-feature" ::
                               "-unchecked" ::
                               "-Xfatal-warnings" ::
                               "-Xlint" ::
                               "-Yno-adapted-args" ::
                               "-Ywarn-dead-code" ::
                               "-Ywarn-numeric-widen" ::
                               "-Ywarn-value-discard" ::
                               "-Xfuture" ::
                               Nil

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {}
)

lazy val commonSettings = Seq()

lazy val spireDeps = {
  val ver = "0.14.1"
  Seq("org.typelevel" %% "spire" % ver)
}

lazy val breeze2 = (project in file("."))
  .aggregate(core)
  .settings(noPublishSettings)

lazy val core = (project in file("breeze2"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= spireDeps)
  .settings(moduleName := "breeze2")