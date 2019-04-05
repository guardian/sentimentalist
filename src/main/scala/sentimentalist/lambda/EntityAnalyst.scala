package sentimentalist.lambda

import sentimentalist.lambda.Cleaner.clean
import software.amazon.awssdk.services.comprehend.ComprehendClient
import software.amazon.awssdk.services.comprehend.model._

object EntityAnalyst {

  private val language = LanguageCode.EN

  def batchEntities(
      comprehend: ComprehendClient
  )(texts: Seq[String]): BatchDetectEntitiesResponse = {
    val request = BatchDetectEntitiesRequest
      .builder()
      .languageCode(language)
      .textList(texts.map(clean): _*)
      .build()
    comprehend.batchDetectEntities(request)
  }
}
