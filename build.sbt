name := "sentimentalist"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "comprehend" % "2.5.21",
  "io.github.mkotsur" %% "aws-lambda-scala" % "0.1.1"
)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith ".properties" =>
    MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
