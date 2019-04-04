package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient

import scala.collection.JavaConverters._

object BatchSentimentAnalystLambda
    extends Lambda[Seq[Case], Seq[AnalysedCase]] {

  private val comprehend = ComprehendClient.builder().build()

  private val moods = SentimentAnalyst.batchMoods(comprehend) _

  override def handle(
      input: Seq[Case],
      context: Context
  ): Either[Throwable, Seq[AnalysedCase]] = {
    val cleanedTexts =
      input.map(c => Cleaner.clean(c.description))
    val results = moods(cleanedTexts)
    Right {
      val resultsAndInputs =
        results.resultList().asScala.toList.zip(input)
      resultsAndInputs map {
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
    }
  }
}
