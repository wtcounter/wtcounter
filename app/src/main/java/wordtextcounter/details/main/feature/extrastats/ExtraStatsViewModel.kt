package wordtextcounter.details.main.feature.extrastats

import android.content.SharedPreferences
import android.text.format.DateUtils
import com.jakewharton.rxrelay2.BehaviorRelay
import com.orhanobut.logger.Logger
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Singles
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.util.Constants
import wordtextcounter.details.main.util.Helper

class ExtraStatsViewModel(private val prefs: SharedPreferences) : BaseViewModel() {

  data class ViewState(val extraStatGroups: List<ExtraStatGroup>)

  val viewState: BehaviorRelay<ViewState> = BehaviorRelay.create()

  private lateinit var characterStatsSingle: Single<Pair<Int, Int>>
  private lateinit var wordsStatsSingle: Single<Triple<Int, Double, Int>>
  private lateinit var sentencesStatSingle: Single<Triple<Int, Int, Int>>

  fun getAllStats(input: String) {
    Logger.d("Prefs is $prefs")
    Logger.d("Prefs value ${prefs.getString("reading_wpm", "-1")}")
    characterStatsSingle = Helper.countCharactersAndSpaces(input).cache()
    wordsStatsSingle = Helper.extraWordStats(input).cache()
    sentencesStatSingle = Helper.extractSentenceStat(input).cache()

    Singles.zip(getBasicStats(input), getExtraStats(), getLengthStats(),
        getTimeStats()) { t1, t2, t3, t4 -> listOf(t1, t2, t3, t4) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { t1: List<ExtraStatGroup>, t2: Throwable? ->
          viewState.accept(
              ViewState(t1))
          t2?.printStackTrace()
        }
  }

  private fun getBasicStats(input: String): Single<ExtraStatGroup> {
    return Singles
        .zip(wordsStatsSingle, characterStatsSingle, Helper.countParagraphs(input),
            sentencesStatSingle, Helper.calculateSize(input))
        { words, characters, paragraphs, sentences, size ->
          val extraStats = mutableListOf<ExtraStat>()
          extraStats.add(
              ExtraStat(R.string.characters,
                  characters.first.toString()))
          extraStats.add(
              ExtraStat(
                  R.string.characters_without_spaces, characters.second.toString()))
          extraStats.add(ExtraStat(R.string.words,
              words.first.toString()))
          extraStats.add(
              ExtraStat(R.string.sentences,
                  sentences.first.toString()))
          extraStats.add(
              ExtraStat(R.string.paragraphs,
                  paragraphs.toString()))
          extraStats.add(ExtraStat(R.string.size,
              size.toString()))

          ExtraStatGroup(R.string.basic_stats,
              extraStats)
        }
  }

  private fun getExtraStats(): Single<ExtraStatGroup> {
    return Singles
        .zip(wordsStatsSingle, characterStatsSingle,
            sentencesStatSingle) { words, characters, sentences ->
          val extraStats = mutableListOf<ExtraStat>()
          extraStats.add(
              ExtraStat(R.string.unique_words,
                  words.third.toString() + " (" + String.format("%.2f",
                      words.third.toDouble() / words.first * 100) + "%)"))
          extraStats.add(
              ExtraStat(
                  R.string.average_word_length, String.format("%.3f", words.second)))
          extraStats.add(ExtraStat(
              R.string.avg_sentence_length_words,
              String.format("%.2f", words.first.toDouble() / sentences.first)))
          extraStats.add(ExtraStat(
              R.string.avg_sentence_length_characters,
              String.format("%.2f", characters.first.toDouble() / sentences.first)))
          ExtraStatGroup(R.string.extra_stats,
              extraStats)
        }
  }

  private fun getLengthStats(): Single<ExtraStatGroup> {
    return sentencesStatSingle.map { sentences ->
      val extraStats = mutableListOf<ExtraStat>()
      extraStats.add(
          ExtraStat(R.string.shortest_sentence,
              sentences.second.toString()))
      extraStats.add(
          ExtraStat(R.string.longest_sentence,
              sentences.third.toString()))
      ExtraStatGroup(R.string.length_stats,
          extraStats)
    }
  }

  private fun getTimeStats(): Single<ExtraStatGroup> {
    return Singles.zip(wordsStatsSingle, characterStatsSingle) { words, characters ->
      val extraStats = mutableListOf<ExtraStat>()
      Logger.d("Prefs $prefs")
      val readingSpeedWordsPerMinute = prefs.getString(
          Constants.PREF_READING_SPEED_WORDS_PER_MINUTE,
          "275").toLong()
      val speakingSpeedWordsPerMinute = prefs.getString(
          Constants.PREF_SPEAKING_SPEED_WORDS_PER_MINUTE,
          "180").toLong()
      val writingSpeedLettersPerSecond = prefs.getString(
          Constants.PREF_WRITING_SPEED_LETTERS_PER_MINUTE,
          "68").toLong()
      extraStats.add(
          ExtraStat(R.string.reading_time,
              DateUtils.formatElapsedTime(
                  words.first * 60 / readingSpeedWordsPerMinute)))
      extraStats.add(
          ExtraStat(R.string.speaking_time,
              DateUtils.formatElapsedTime(words.first * 60 / speakingSpeedWordsPerMinute)))
      extraStats.add(
          ExtraStat(R.string.writing_time,
              DateUtils.formatElapsedTime(characters.first * 60 / writingSpeedLettersPerSecond)))
      ExtraStatGroup(R.string.time_stats,
          extraStats)
    }
  }
}