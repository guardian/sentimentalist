package sentimentalist.lambda

import java.text.SimpleDateFormat
import java.util.Date

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient
import software.amazon.awssdk.services.s3.S3Client

import scala.collection.JavaConverters._

object BatchSentimentAnalystLambda extends Lambda[Seq[Case], Seq[AnalysedCase]] {

  private val comprehend = ComprehendClient.builder().build()
  private val s3 = S3Client.builder().build()

  private val moods = SentimentAnalyst.batchMoods(comprehend) _
  private val uploadToS3 = S3Uploader.upload(s3, "gu-sentimentalist") _

  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")

  private def s3Key(): String = {
    val date = dateFormatter.format(new Date())
    val timestamp = System.currentTimeMillis
    s"date=$date/$timestamp"
  }

  override def handle(input: Seq[Case], context: Context): Either[Throwable, Seq[AnalysedCase]] = {
    val results = moods(input map (_.description))
    val resultsAndInputs = results.resultList().asScala.toList.zip(input)
    val analysedCases = AnalysedCase.fromResultsAndInputs(resultsAndInputs)
    uploadToS3(s3Key(), analysedCases)
    Right(analysedCases)
  }
}
