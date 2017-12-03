package wordtextcounter.details.main.util

import android.util.Log

/**
 * Created by hirak on 03/12/17.
 */
public class Helper {
  companion object {
    public fun countWords(input: String): Int {
      var s: String = input
      s = s.replace("\n", " ")
      Log.d("String ", s)
      s = s.replace("\t", " ")
      Log.d("String ", s)
      s = s.trim().replace(" +".toRegex(), " ")
      Log.d("String ", s)

      var noOfWords = 0
      for (c in s) {
        if (c == ' ') {
          noOfWords++
        }
      }
      return noOfWords + 1
    }
  }
}