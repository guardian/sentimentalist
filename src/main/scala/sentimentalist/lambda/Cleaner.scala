package sentimentalist.lambda

object Cleaner {

  private val whitelist = Seq(
    "a",
    "bad",
    "corbyn",
    "donald",
    "good",
    "is",
    "jeremy",
    "may",
    "minister",
    "prime",
    "the",
    "theresa",
    "times",
    "tusk",
    "week"
  )

  def clean(text: String): String = {
    val words = text.split("\\b")
    val validWords = words.filter(word => whitelist.contains(word.toLowerCase))
    val cleaned = validWords.mkString(" ")
    cleaned
  }
}
