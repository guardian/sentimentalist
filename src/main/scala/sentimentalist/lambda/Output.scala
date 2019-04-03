package sentimentalist.lambda

case class Output(
    id: String,
    text: String,
    sentiment: String,
    sentimentScore: SentimentScore
)
