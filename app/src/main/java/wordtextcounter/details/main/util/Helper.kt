package wordtextcounter.details.main.util

import android.text.format.Formatter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import wordtextcounter.details.main.App
import java.nio.charset.Charset
import java.text.BreakIterator

object Helper {

  fun countWords(input: String): Single<Int> {
    return Single.fromCallable {
      getNoOfWords(input)
    }.subscribeOn(Schedulers.computation())
  }

  fun countCharacters(input: String): Single<Int> {
    return Single.fromCallable {
      var graphemeCount = 0
      val graphemeCounter = BreakIterator.getCharacterInstance()
      graphemeCounter.setText(input)
      while (graphemeCounter.next() != BreakIterator.DONE) {
        graphemeCount++
      }
      graphemeCount
    }.subscribeOn(Schedulers.computation())
  }

  fun countCharactersAndSpaces(input: String): Single<Pair<Int, Int>> {
    return Single.fromCallable {
      var graphemeCount = 0
      var whiteSpaceCount = 0
      val graphemeCounter = BreakIterator.getCharacterInstance()
      graphemeCounter.setText(input)
      var start = 0
      var end = graphemeCounter.next()
      while (end != BreakIterator.DONE) {
        graphemeCount++
        if (Character.isWhitespace(input[start])) {
          whiteSpaceCount++
        }
        start = end
        end = graphemeCounter.next()
      }
      Pair(graphemeCount, graphemeCount - whiteSpaceCount)
    }.subscribeOn(Schedulers.computation())
  }

  fun countSentences(input: String): Single<Int> {
    return Single.fromCallable {
      var graphemeCount = 0
      val boundary = BreakIterator.getSentenceInstance()
      boundary.setText(input)
      var start = 0
      var end = boundary.next()
      while (end != BreakIterator.DONE) {
        val sentence = input.substring(start, end)
        if (sentence.isNotBlank()) {
          graphemeCount++
        }
        start = end
        end = boundary.next()
      }
      graphemeCount
    }.subscribeOn(Schedulers.computation())
  }

  /**
   * This will return triple where first is total word count, second is avg word length and third is unique words in text
   */
  fun extraWordStats(input: String): Single<Triple<Int, Double, Int>> {
    return Single.fromCallable {
      val wordIterator = BreakIterator.getWordInstance()
      val wordSet = mutableSetOf<String>()
      wordIterator.setText(input)
      var start = wordIterator.first()
      var end = wordIterator.next()
      var wordCount = 0
      var totalLength = 0
      while (end != BreakIterator.DONE) {
        val word = input.substring(start, end)
        if (Character.isLetterOrDigit(word[0])) {
          wordSet.add(word.toLowerCase())
          wordCount++
          totalLength += word.length
        }
        start = end
        end = wordIterator.next()
      }
      Triple(wordCount, totalLength.toDouble() / wordCount, wordSet.size)
    }.subscribeOn(Schedulers.computation())
  }


  fun extractSentenceStat(input: String): Single<Triple<Int, Int, Int>> {
    return Single.fromCallable {
      var minSentenceLength = Int.MAX_VALUE
      var maxSentenceLength = 0
      var totalSentences = 0
      val boundary = BreakIterator.getSentenceInstance()
      boundary.setText(input)
      var start = boundary.first()
      var end = boundary.next()
      while (end != BreakIterator.DONE) {
        val sentence = input.substring(start, end)
        if (sentence.isNotBlank()) {
          totalSentences++
          val wordLength = getNoOfWords(sentence)
          if (wordLength != 0) {
            if (wordLength < minSentenceLength) {
              minSentenceLength = wordLength
            }
            if (wordLength > maxSentenceLength) {
              maxSentenceLength = wordLength
            }
          }
        }
        start = end
        end = boundary.next()
      }
      Triple(totalSentences, minSentenceLength, maxSentenceLength)
    }.subscribeOn(Schedulers.computation())
  }

  fun countParagraphs(input: String): Single<Int> {
    return Single.fromCallable {
      var s: String = input
      s = s.replace("\n+".toRegex(), "\n")

      var noOfParagraphs = 0
      for (c in s) {
        if (c == '\n') {
          noOfParagraphs++
        }
      }
      noOfParagraphs + 1
    }.subscribeOn(Schedulers.computation())
  }

  fun calculateSize(input: String, selectedCharSet: Charset): Single<String> {
    return Single.fromCallable {
      Formatter.formatFileSize(App.getAppInstance(),
          input.toByteArray(selectedCharSet).size.toLong())
    }.subscribeOn(Schedulers.computation())
  }

  private fun getNoOfWords(input: String): Int {
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
}