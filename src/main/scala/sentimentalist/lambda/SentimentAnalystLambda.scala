package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._

object SentimentAnalystLambda extends Lambda[Input, Output] {

  override def handle(
      input: Input,
      context: Context
  ): Either[Throwable, Output] = {
    val cleanedText = Cleaner.clean(input.text)
    val singleResult = SentimentAnalyst.mood(cleanedText)
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
