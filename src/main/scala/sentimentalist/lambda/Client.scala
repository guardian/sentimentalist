package sentimentalist.lambda

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.comprehend.ComprehendClient

object Client extends App {

//  val result = SentimentAnalyst.batchMoods(
//    Seq(
//      "The Guardian is great!",
//      "The Guardian is terrible!",
//      "The Guardian is good and very bad depending on the day",
//      "The Guardian is a newspaper"
//    )
//  )
//  result.resultList().forEach(println)

  private val credProvider = ProfileCredentialsProvider.create("developerPlayground")

  private val region = Region.EU_WEST_1

  private val comprehend = ComprehendClient
    .builder()
    .credentialsProvider(credProvider)
    .region(region)
    .build()

  val text = "The Guardian is great!"
  val singleResult = SentimentAnalyst.mood(comprehend)(text)
  println(singleResult.sentimentAsString())
  println(singleResult.sentimentScore())
}
