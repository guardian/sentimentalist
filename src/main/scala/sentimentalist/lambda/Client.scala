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

  val text = "There are only so many times you can – or should – give someone the benefit of the " +
    "doubt. In asking Donald Tusk and the European council for a ludicrous, pointless " +
    "extension just until June, Theresa May has surrendered any right to be seen as a " +
    "prime minister acting in good faith.\n\nThe truth is that the last five months have" +
    " been – by far – the worst in modern British politics. Because of May. From the " +
    "moment that she pulled the vote on her withdrawal agreement (because she knew that " +
    "our democratic institutions would not consent to it) she has consistently placed " +
    "her own, narrow interests above those of this country. She has ignored and misled " +
    "parliament. She has acted as a demagogue, giving licence to those who threaten and " +
    "harass MPs. She has burned bridges with our European partners and has treated the " +
    "British people with contempt.\n\nStaying just until June solves none of the " +
    "problems that her Brexit deal creates. It constructs yet another melodramatic " +
    "cliff-edge, which she hopes to use as yet another threat in yet another vote on the" +
    " same deal that parliament has rejected three times. It will mean electing MEPs in " +
    "full knowledge (hope, even) that they will be ejected just a month later. It places" +
    " no deal – with all the associated economic and human harm – back on the table" +
    ".\n\nWhy has she done this now? She did not need to – parliament is in the process " +
    "of legislating for a long extension, the European council does not meet until next " +
    "week. What is more, this makes a mockery of the negotiations that May is supposedly" +
    " conducting with Jeremy Corbyn as I write. What is the point of these discussions " +
    "if she is going to act unilaterally against the expressed wishes of Labour and of " +
    "parliament?\n\nThe truth is that deep down we all – even those of us who have up " +
    "till now given her the benefit of the doubt – know why. She has done this now " +
    "because she cannot let go of the idea that she may yet triumph in her borderline " +
    "abusive relationship with parliament and the country. Failing that, she hopes to " +
    "place the blame for a long extension on everyone except herself.\n\nFor months now " +
    "I have travelled the country, speaking three or four times a week at meetings about" +
    " Brexit. The people who show up to town and village halls in the rain on a school " +
    "night to join me are desperately afraid. I tell them not to worry, that May is a " +
    "ball of wool that unravels – without exception – every time she moves. That is true" +
    " of this latest outrage. She will not get what she is demanding. But the truth is " +
    "that it is no longer merely May or her degenerate party that unravels any more. It " +
    "is this country. Our economy, our wellbeing, our status in the world.\n\nMay is now" +
    " systematically undermining the national interest. Jeremy Corbyn, our parliament, " +
    "Donald Tusk and our European partners can stop her. The time has come to pull the " +
    "plug."
  val singleResult = SentimentAnalyst.mood(comprehend)(text)
  println(singleResult.sentimentAsString())
  println(singleResult.sentimentScore())

  val entities = EntityAnalyst.batchEntities(comprehend)(Seq(text))
  println(entities)
}
