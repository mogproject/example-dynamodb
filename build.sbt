name := "example-dynamodb"

version := "1.0"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.8.7"
)

javaOptions in run ++= Seq(
  "-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog",
  "-Dorg.apache.commons.logging.simplelog.showdatetime=true",
  "-Dorg.apache.commons.logging.simplelog.log.org.apache.http.wire=DEBUG"
)

fork in run := true