package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient

object SentimentAnalystLambda extends Lambda[Input, Output] {

  private val comprehend = ComprehendClient.builder().build()

  private val mood = SentimentAnalyst.mood(comprehend) _

  override def handle(
      input: Input,
      context: Context
  ): Either[Throwable, Output] = {
    val cleanedText = Cleaner.clean(input.text)
    val singleResult = mood(cleanedText)
    Right(
      Output(
        input.id,
        input.text,
        singleResult.sentimentAsString(),
        SentimentScore.fromAwsScore(singleResult.sentimentScore())
      )
    )
  }
}
