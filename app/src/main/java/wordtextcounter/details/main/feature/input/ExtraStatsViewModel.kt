package wordtextcounter.details.main.feature.input

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Singles
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.util.Helper

class ExtraStatsViewModel : BaseViewModel() {

  data class ViewState(val extraStatGroups: List<ExtraStatGroup>)

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  fun getAllStats(input: String) {

    Single.zip(getBasicStats(input), getExtraStats(input),
        BiFunction<ExtraStatGroup, ExtraStatGroup, List<ExtraStatGroup>> { t1, t2 ->
          return@BiFunction listOf(t1, t2)
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { t1: List<ExtraStatGroup>, t2: Throwable? ->
          viewState.accept(ViewState(t1))
          t2?.printStackTrace()
        }
  }

  private fun getBasicStats(input: String): Single<ExtraStatGroup> {
    return Singles
        .zip(Helper.countWords(input), Helper.countCharactersAndSpaces(input),
            Helper.countParagraphs(input),
            Helper.countSentences(input),
            Helper.calculateSize(input))
        { words, characters, paragraphs, sentences, size ->
          val extraStats = mutableListOf<ExtraStat>()
          extraStats.add(ExtraStat(R.string.characters, characters.first.toString()))
          extraStats.add(
              ExtraStat(R.string.characters_without_spaces, characters.second.toString()))
          extraStats.add(ExtraStat(R.string.words, words.toString()))
          extraStats.add(ExtraStat(R.string.sentences, sentences.toString()))
          extraStats.add(ExtraStat(R.string.paragraphs, paragraphs.toString()))
          extraStats.add(ExtraStat(R.string.size, size.toString()))

          ExtraStatGroup(R.string.basic_stats, extraStats)
        }
  }

  private fun getExtraStats(input: String): Single<ExtraStatGroup> {
    return Singles
        .zip(Helper.extraWordStats(input), Helper.countCharacters(input),
            Helper.countSentences(input)
        ) { words, characters, sentences ->
          val extraStats = mutableListOf<ExtraStat>()
          extraStats.add(ExtraStat(R.string.unique_words,
              words.third.toString() + " (" + String.format("%.2f",
                  words.third.toDouble() / words.first * 100) + "%)"))
          extraStats.add(
              ExtraStat(R.string.average_word_length, String.format("%.3f", words.second)))
          extraStats.add(ExtraStat(R.string.avg_sentence_length_words,
              (words.first.toDouble() / sentences).toString()))
          extraStats.add(ExtraStat(R.string.avg_sentence_length_characters,
              (characters.toDouble() / sentences).toString()))
          ExtraStatGroup(R.string.extra_stats, extraStats)
        }
  }
}