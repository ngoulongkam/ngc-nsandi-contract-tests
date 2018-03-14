name := "ngc-nsandi-contract-tests"

scalaVersion := "2.11.12"

fork in Test := false
parallelExecution in Test := false
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/html-report")
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports")
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

val playVersion = "2.5.18"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test",
  "com.typesafe.play" %% "play-test" % playVersion % "test",
  "com.typesafe.play" %% "play-ws" % playVersion % "test",
  "uk.gov.hmrc" %% "domain" % "5.1.0" % "test",
  "uk.gov.hmrc" %% "http-verbs" % "7.2.0" % "test",
  "uk.gov.hmrc" %% "http-verbs-play-25" % "0.9.0" % "test",
  // If you are considering upgrading to scala-uri 1.0.0 note that it contains a breaking change that affects this project, see https://github.com/lemonlabsuk/scala-uri/issues/16
  "io.lemonlabs" %% "scala-uri" % "0.5.7" % "test"
)
