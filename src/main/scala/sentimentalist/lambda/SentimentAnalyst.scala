package sentimentalist.lambda

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.comprehend.ComprehendClient
import software.amazon.awssdk.services.comprehend.model._

object SentimentAnalyst {

  private val credProvider =
    ProfileCredentialsProvider.create("developerPlayground")

  private val region = Region.EU_WEST_1

  private val language = LanguageCode.EN

  private val comprehend = ComprehendClient
    .builder()
    .credentialsProvider(credProvider)
    .region(region)
    .build()

  def mood(text: String): DetectSentimentResponse = {
    val request = DetectSentimentRequest
      .builder()
      .languageCode(language)
      .text(text)
      .build()
    comprehend.detectSentiment(request)
  }

  def batchMoods(texts: Seq[String]): BatchDetectSentimentResponse = {
    val request = BatchDetectSentimentRequest
      .builder()
      .languageCode(language)
      .textList(texts: _*)
      .build()
    comprehend.batchDetectSentiment(request)
  }
}
