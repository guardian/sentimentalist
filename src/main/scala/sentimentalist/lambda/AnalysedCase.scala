package sentimentalist.lambda

import software.amazon.awssdk.services.comprehend.model.{
  BatchDetectEntitiesItemResult,
  BatchDetectSentimentItemResult
}

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

  def fromInputsAndResults(
      inputsAndResults: Seq[(Case, BatchDetectSentimentItemResult, BatchDetectEntitiesItemResult)]
  ): Seq[AnalysedCase] =
    inputsAndResults map {
      case (caseRecord, sentimentAnalysis, entityAnalysis) =>
        AnalysedCase(
          epoch = 1,
          inputType = "CSR Case",
          inputId = caseRecord.caseId.toString,
          inputText = Cleaner.clean(caseRecord.description),
          overallSentiment = sentimentAnalysis.sentimentAsString(),
          sentimentBreakdown = SentimentScore.fromAwsScore(sentimentAnalysis.sentimentScore()),
          entities = Entity.fromAnalysis(entityAnalysis)
        )
    }
}
