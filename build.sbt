name := "finch-kantan"

version := "0.1"

scalaVersion := "2.12.8"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

organization := "io.github.hderms"

homepage := Some(url("https://github.com/hderms/finch-kantan"))

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core"  % "0.26.0",
   "com.nrinaudo" %% "kantan.csv" % "0.5.0",
  "org.specs2" %% "specs2-core" % "4.3.3" % "test"
)
