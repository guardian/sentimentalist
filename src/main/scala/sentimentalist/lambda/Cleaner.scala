package sentimentalist.lambda

object Cleaner {

  private val whitelist = Seq("the", "good", "bad", "is")

  def clean(text: String): String = {
    val words = text.split("\\b")
    val validWords = words.filter(word => whitelist.contains(word.toLowerCase))
    val cleaned = validWords.mkString(" ")
    cleaned
  }
}
