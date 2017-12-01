lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.sqrt26",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "wish-upon-a-wm",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.rogach" %% "scallop" % "3.1.1"
  )
