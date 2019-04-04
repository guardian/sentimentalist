package sentimentalist.lambda

case class AnalysedCase(
    epoch: String,
    inputType: String,
    inputId: String,
    inputText: String,
    overallSentiment: String,
    sentimentBreakdown: SentimentScore,
    entities: Seq[Entity]
)
