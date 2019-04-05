package sentimentalist.lambda

import software.amazon.awssdk.services.comprehend.model.BatchDetectSentimentItemResult

case class AnalysedCase(
    epoch: Long,
    inputType: String,
    inputId: String,
    inputText: String,
    overallSentiment: String,
    sentimentBreakdown: SentimentScore,
    entities: Seq[Entity]
)

object AnalysedCase {

  def fromResultsAndInputs(
      resultsAndInputs: Seq[(BatchDetectSentimentItemResult, Case)]
  ): Seq[AnalysedCase] =
    resultsAndInputs map {
      case (result, caseRecord) =>
        AnalysedCase(
          epoch = 1,
          inputType = "CSR Case",
          inputId = caseRecord.caseId.toString,
          inputText = Cleaner.clean(caseRecord.description),
          overallSentiment = result.sentimentAsString(),
          sentimentBreakdown = SentimentScore.fromAwsScore(result.sentimentScore()),
          entities = Nil
        )
    }
}
