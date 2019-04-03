package sentimentalist.lambda

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

  val text = "The Guardian is great!"
  val cleanedText = Cleaner.clean(text)
  val singleResult = SentimentAnalyst.mood(cleanedText)
  println(singleResult.sentimentAsString())
  println(singleResult.sentimentScore())
}
