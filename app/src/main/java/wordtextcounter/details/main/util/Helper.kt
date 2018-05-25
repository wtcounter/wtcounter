package wordtextcounter.details.main.util

object Helper {

  fun countWords(input: String): Int {
    var s: String = input
    s = s.replace("\n", " ")
    s = s.replace("\t", " ")
    s = s.trim().replace(" +".toRegex(), " ")

    var noOfWords = 0
    for (c in s) {
      if (c == ' ') {
        noOfWords++
      }
    }
    return noOfWords + 1
  }

  fun countSentences(input: String): Int {
    var s: String = input
    s = s.replace("\n", " ")
    s = s.replace("\t", " ")
    s = s.trim().replace(" +".toRegex(), " ")

    var noOfSentences = 0
    for (c in s) {
      if (c == '.' || c == '\n') {
        noOfSentences++
      }
    }
    return noOfSentences
  }

  fun countParagraphs(input: String): Int {
    var s: String = input
    s = s.replace("\n+".toRegex(), "\n")

    var noOfParagraphs = 0
    for (c in s) {
      if (c == '\n') {
        noOfParagraphs++
      }
    }
    return noOfParagraphs + 1
  }

  fun calculateSize(input: String): String {
    return input.toByteArray(Charsets.UTF_8).size.toString() + " B"
  }
}