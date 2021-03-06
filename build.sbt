name := "sentimentalist"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "comprehend" % "2.5.21",
  "software.amazon.awssdk" % "s3" % "2.5.23",
  "io.github.mkotsur" %% "aws-lambda-scala" % "0.1.1"
)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith ".properties" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".config" =>
    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".json" =>
    MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
