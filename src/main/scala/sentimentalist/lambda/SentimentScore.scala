package sentimentalist.lambda

import software.amazon.awssdk.services.comprehend.model.{
  SentimentScore => AwsScore
}

case class SentimentScore(
    positive: Double,
    negative: Double,
    mixed: Double,
    neutral: Double
)

object SentimentScore {
  def fromAwsScore(score: AwsScore) = SentimentScore(
    positive = score.positive().toDouble,
    negative = score.negative().toDouble,
    mixed = score.mixed().toDouble,
    neutral = score.neutral().toDouble
  )
}
