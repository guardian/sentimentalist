package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient

import scala.collection.JavaConverters._

object BatchSentimentAnalystLambda extends Lambda[Seq[Input], Seq[Output]] {

  private val comprehend = ComprehendClient.builder().build()

  private val moods = SentimentAnalyst.batchMoods(comprehend) _

  override def handle(
      input: Seq[Input],
      context: Context
  ): Either[Throwable, Seq[Output]] = {
    val cleanedTexts = input.map(in => Cleaner.clean(in.text))
    val results = moods(cleanedTexts)
    Right {
      val resultsAndInputs = results.resultList().asScala.toList.zip(input)
      resultsAndInputs map {
        case (result, inputItem) =>
          Output(
            id = inputItem.id,
            text = inputItem.text,
            sentiment = result.sentimentAsString(),
            sentimentScore =
              SentimentScore.fromAwsScore(result.sentimentScore())
          )
      }
    }
  }
}
