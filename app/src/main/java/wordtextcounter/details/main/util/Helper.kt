package wordtextcounter.details.main.util

import java.text.BreakIterator

object Helper {

  fun countWords(input: String): Int {

    val wordIterator = BreakIterator.getWordInstance()
    wordIterator.setText(input)
    var start = wordIterator.first()
    var end = wordIterator.next()

    var wordCount = 0
    while (end != BreakIterator.DONE) {
      val word = input.substring(start, end)
      if (Character.isLetterOrDigit(word[0])) {
        wordCount++
      }
      start = end
      end = wordIterator.next()
    }

    return wordCount

  }


  fun countCharacters(input: String): Int {

    var graphemeCount = 0
    val graphemeCounter = BreakIterator.getCharacterInstance()
    graphemeCounter.setText(input)
    while (graphemeCounter.next() != BreakIterator.DONE) {
      graphemeCount++
    }

    return graphemeCount

  }


  fun countSentences(input: String): Int {

    var graphemeCount = 0
    val boundary = BreakIterator.getSentenceInstance()
    boundary.setText(input)
    var end = boundary.next()
    while (end != BreakIterator.DONE) {
      graphemeCount++
      end = boundary.next()
    }
    return graphemeCount

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