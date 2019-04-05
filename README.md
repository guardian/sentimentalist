# Sentimentalist

## To deploy lambda
1. Run `sbt assembly`
1. Create new lambda function in AWS console
1. In console, upload `target/scala-2.12/sentimentalist-assembly-0.1.0-SNAPSHOT.jar` to new lambda
1. In console, set 'Handler' field to `sentimentalist.lambda.BatchSentimentAnalystLambda::handle`

The execution role of the lambda will need access to the `Comprehend` and `S3` services.
