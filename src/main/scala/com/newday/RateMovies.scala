package com.newday

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{AnalysisException, SparkSession}

import scala.util.Failure

class MovieRating {

  //create spark session
  private val spark = SparkSession
    .builder
    .master("local")
    .appName("movieRating")
    .config("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider")
    .getOrCreate()

  def RateMovies(): Unit = {

    val movieSchema = StructType(Array(
      StructField("MovieID", StringType, true),
      StructField("Title", StringType, true),
      StructField("Genres", StringType, true)))

    val ratingSchema = StructType(Array(
      StructField("UserID", StringType, true),
      StructField("MovieID", StringType, true),
      StructField("Rating", StringType, true),
      StructField("Timestamp", IntegerType, true)
    ))

    try {

      import spark.implicits._

      var movieDF = spark.sqlContext.read
        .schema(movieSchema)
        .option("delimiter", "\t")
        .csv(spark.sqlContext.read.textFile("s3a://nd-movielens/movies.dat")
          .map(line => line.split("\\:\\:").mkString("\t")))

      movieDF.show()

      var ratingDF = spark.sqlContext.read
        .schema(ratingSchema)
        .option("delimiter", "\t")
        .csv(spark.sqlContext.read.textFile("s3a://nd-movielens/ratings.dat")
          .map(line => line.split("\\:\\:").mkString("\t")))

      import org.apache.spark.sql.functions._

      val movieRatings = movieDF.join(ratingDF, Seq("MovieID")).distinct().groupBy("MovieID", "Title", "Genres")
        .agg(max("Rating").as("max_rating"), min("Rating").as("min_rating"), avg("Rating").as("avg_rating"))

      movieRatings
        .coalesce(1)
        .write.mode(org.apache.spark.sql.SaveMode.Overwrite)
        .parquet("data/movieRatings.parquet")

      import org.apache.spark.sql.expressions.Window

      val w = Window.partitionBy($"UserID")
      val ratingDesc = row_number().over(w.orderBy($"Rating".desc)).alias("rank")
      val topMoviesDF = ratingDF.select($"UserID", ratingDesc).filter($"rank" <= 3).drop("rank")

      topMoviesDF
        .coalesce(1)
        .write.mode(org.apache.spark.sql.SaveMode.Overwrite)
        .parquet("data/topMovies.parquet")

    } catch {
      case e: AnalysisException => {
        println(e)
      }
      case unknown: Exception => {
        println("UNKNOWN EXCEPTION.")
        println(Failure(unknown))
      }
    }
  }
}

object RateMovies {
  def main(args: Array[String]): Unit = {
    val mr = new MovieRating()
    mr.RateMovies()
  }
}