package wordtextcounter.details.main.util

import com.orhanobut.logger.Logger
import java.text.BreakIterator

object Helper {

  fun countWords(input: String): Int {
//    var s: String = input
//    s = s.replace("\n", " ")
//    s = s.replace("\t", " ")
//    s = s.trim().replace(" +".toRegex(), " ")
//
//    var noOfWords = 0
//    for (c in s) {
//      if (c == ' ') {
//        noOfWords++
//      }
//    }
//    return noOfWords + 1


    var s = input


    var wordCount = 0
    val graphemeCounter = BreakIterator.getWordInstance()
    graphemeCounter.setText(s)
    while (graphemeCounter.next() != BreakIterator.DONE) {
      wordCount++
    }

    Logger.d("Word count $wordCount")


//    s = s.replace("[^-<(\\[{^=\$!|\\]})?*+.>]+|[^\\s]+".toRegex(), "")

//    Logger.d("After removing special characters spaces count ${s}")

//    Logger.d("After removing special characters spaces count ${s.length}")
//    val noOfWords = s.length
    Logger.d("total length ${s.length}")


    Logger.d("Space removed string ${s.replace("\\s+".toRegex(), "")}")

    Logger.d("Space removed string length " + s.replace("\\s+".toRegex(), "").length)



    return wordCount - (s.length - s.replace("\\s+".toRegex(), "").replace(
        "[-<~/:_@#%&(\\[{^=\$!|\\]})?*+;'\".>]".toRegex(), "").length)
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
    val s: String = input
    val sentences = s.split("[!?.:]+".toRegex())

    //decrease count if last sentence is  an empty string
    return if (sentences[sentences.size - 1].trim().isEmpty()) {
      sentences.size - 1
    } else {
      sentences.size
    }
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