name := """virtualcoach-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.+",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "x-editable-bootstrap3" % "1.5.1",
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "bootstrap-glyphicons" % "bdd2cbfba0",
  "org.webjars" % "chartjs" % "26962ce"
)


includeFilter in (Assets, LessKeys.less) := "*.less"

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node