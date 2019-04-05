package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.circe.syntax._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient
import software.amazon.awssdk.services.s3.S3Client

import scala.collection.JavaConverters._

object BatchSentimentAnalystLambda
    extends Lambda[Seq[Case], Seq[AnalysedCase]] {

  private val comprehend = ComprehendClient.builder().build()
  private val s3 = S3Client.builder().build()

  private val moods = SentimentAnalyst.batchMoods(comprehend) _
  private val uploadToS3 = S3Uploader.upload(s3, "gu-sentimentalist") _

  override def handle(
      input: Seq[Case],
      context: Context
  ): Either[Throwable, Seq[AnalysedCase]] = {
    val cleanedTexts =
      input.map(caseRecord => Cleaner.clean(caseRecord.description))
    val results = moods(cleanedTexts)
    val resultsAndInputs =
      results.resultList().asScala.toList.zip(input)
    val analysedCases = resultsAndInputs map {
      case (result, caseRecord) =>
        AnalysedCase(
          epoch = "1",
          inputType = "CSR Case",
          inputId = caseRecord.caseId.toString,
          inputText = caseRecord.description,
          overallSentiment = result.sentimentAsString(),
          sentimentBreakdown =
            SentimentScore.fromAwsScore(result.sentimentScore()),
          entities = Nil
        )
    }
    uploadToS3("test-file", analysedCases.asJson.noSpaces)
    Right(analysedCases)
  }
}
