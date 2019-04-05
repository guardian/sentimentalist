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
  private val entities = EntityAnalyst.batchEntities(comprehend) _
  private val uploadToS3 = S3Uploader.upload(s3, "gu-sentimentalist") _

  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")

  private def s3Key(): String = {
    val date = dateFormatter.format(new Date())
    val timestamp = System.currentTimeMillis
    s"date=$date/$timestamp"
  }

  override def handle(input: Seq[Case], context: Context): Either[Throwable, Seq[AnalysedCase]] = {
    val texts = input map (_.description)
    val sentimentAnalysis = moods(texts).resultList().asScala.toList
    val entityAnalysis = entities(texts).resultList().asScala.toList
    val inputsAndResults = (input, sentimentAnalysis, entityAnalysis).zipped.toList
    val analysedCases = AnalysedCase.fromInputsAndResults(inputsAndResults)
    uploadToS3(s3Key(), analysedCases)
    Right(analysedCases)
  }
}
