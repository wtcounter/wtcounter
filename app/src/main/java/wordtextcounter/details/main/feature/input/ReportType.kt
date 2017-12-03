package wordtextcounter.details.main.feature.input

import android.support.annotation.StringRes
import wordtextcounter.details.main.R

enum class ReportType(var i: Int, @StringRes var characters: Int) {
  CHARS(1, R.string.characters),
  WORDS(1, R.string.words),
  PARAGRAPHS(1, R.string.paragraphs),
  SIZE(1, R.string.size)

}