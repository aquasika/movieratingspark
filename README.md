# movieratingspark

#Spark-submit to run in local mode

<spark-bin>/spark-submit  --name "Spark job to rate movies" \
        --class "com.newday.RateMovies" \
         ratemovies.jar
         

#Spark-submit to run over standalone cluster. Before using this edit Spark session to not use local.

<spark-bin>/spark-submit  --name "Spark job to rate movies" \
        --class "com.newday.RateMovies" \
        --master <master-url>
         ratemovies.jar
