name := "movieratingspark"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.0",
  "org.apache.spark" %% "spark-core" % "2.4.0",
  "org.apache.hadoop" % "hadoop-aws" % "2.8.5"
//  "com.holdenkarau" %% "spark-testing-base" % "2.3.1_0.12.0" % "test",
//  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.7"
)