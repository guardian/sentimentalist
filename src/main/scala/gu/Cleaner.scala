package gu

object Cleaner {

  private val whitelist = Seq("the")

  def clean(text:String):String = {
    val words = text.split("\\b")
    val validWords = words.filter(word => whitelist.contains(word.toLowerCase))
    val cleaned = validWords.mkString(" ")
    cleaned
  }
}
