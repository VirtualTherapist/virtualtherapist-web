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
  "org.webjars" % "jquery" % "1.*",
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "x-editable-bootstrap3" % "1.5.1",
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "bootstrap-glyphicons" % "bdd2cbfba0",
  "org.webjars" % "chartjs" % "26962ce",
  "com.wordnik" %% "swagger-play2" % "1.3.10" exclude("org.reflections", "reflections"),
  //"org.apache.solr" % "solr-solrj" % "4.3.1",
  "org.apache.lucene" % "lucene-core" % "4.10.2",
  "org.apache.lucene" % "lucene-analyzers-common" % "4.10.2",
  //"org.reflections" % "reflections" % "0.9.8" notTransitive (),
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.14" % "test"
)


includeFilter in (Assets, LessKeys.less) := "*.less"

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

herokuAppName in Compile := "virtualcoach"