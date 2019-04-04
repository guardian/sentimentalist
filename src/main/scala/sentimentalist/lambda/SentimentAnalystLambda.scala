package sentimentalist.lambda

import com.amazonaws.services.lambda.runtime.Context
import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._
import software.amazon.awssdk.services.comprehend.ComprehendClient

object SentimentAnalystLambda extends Lambda[Case, AnalysedCase] {

  private val comprehend = ComprehendClient.builder().build()

  private val mood = SentimentAnalyst.mood(comprehend) _

  override def handle(
      input: Case,
      context: Context
  ): Either[Throwable, AnalysedCase] = {
    val cleanedText = Cleaner.clean(input.description)
    val singleResult = mood(cleanedText)
    Right(
      AnalysedCase(
        epoch = "1",
        inputType = "CSR Case",
        inputId = input.caseId.toString,
        inputText = input.description,
        overallSentiment = singleResult.sentimentAsString(),
        sentimentBreakdown =
          SentimentScore.fromAwsScore(singleResult.sentimentScore()),
        entities = Nil
      )
    )
  }
}
