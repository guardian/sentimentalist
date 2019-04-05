# Sentimentalist

## To deploy lambda
0. Run `sbt assembly`
0. Create new lambda function in AWS console
0. In console, upload `target/scala-2.12/sentimentalist-assembly-0.1.0-SNAPSHOT.jar` to new lambda
0. In console, set 'Handler' field to `sentimentalist.lambda.BatchSentimentAnalystLambda::handle`

The execution role of the lambda will need access to the `Comprehend` and `S3` services.
