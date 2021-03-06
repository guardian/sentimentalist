package sentimentalist.lambda

import sentimentalist.lambda.Cleaner.clean
import software.amazon.awssdk.services.comprehend.ComprehendClient
import software.amazon.awssdk.services.comprehend.model._

object SentimentAnalyst {

  private val language = LanguageCode.EN

  def mood(comprehend: ComprehendClient)(text: String): DetectSentimentResponse = {
    val request = DetectSentimentRequest
      .builder()
      .languageCode(language)
      .text(clean(text))
      .build()
    comprehend.detectSentiment(request)
  }

  def batchMoods(comprehend: ComprehendClient)(texts: Seq[String]): BatchDetectSentimentResponse = {
    val request = BatchDetectSentimentRequest
      .builder()
      .languageCode(language)
      .textList(texts map clean: _*)
      .build()
    comprehend.batchDetectSentiment(request)
  }
}
