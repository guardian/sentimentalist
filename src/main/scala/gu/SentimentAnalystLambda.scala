package gu

import io.circe.generic.auto._
import io.github.mkotsur.aws.handler.Lambda
import io.github.mkotsur.aws.handler.Lambda._

object SentimentAnalystLambda extends Lambda[Input, Output] {

//  val result = SentimentAnalyst.batchMoods(
//    Seq(
//      "The Guardian is great!",
//      "The Guardian is terrible!",
//      "The Guardian is good and very bad depending on the day",
//      "The Guardian is a newspaper"
//    )
//  )
//  result.resultList().forEach(println)

  val text = "The Guardian is great!"
  val cleanedText = Cleaner.clean(text)
  val singleResult = SentimentAnalyst.mood(cleanedText)
  println(singleResult.sentimentAsString())
  println(singleResult.sentimentScore())
}
