name := "movieratingspark"

version := "0.1"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.0",
  "org.apache.spark" %% "spark-core" % "2.4.0",
  "org.apache.spark" %% "spark-catalyst" % "2.4.0",
  "org.apache.hadoop" % "hadoop-aws" % "2.8.5"
)

//mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
//{
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x => MergeStrategy.first
//}
//}