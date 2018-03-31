package wordtextcounter.details.main.feature.notes

import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.BehaviorRelay
import kotlinx.android.synthetic.main.item_note.view.foldingCell
import kotlinx.android.synthetic.main.item_note.view.ibEdit
import kotlinx.android.synthetic.main.item_note.view.ibShare
import kotlinx.android.synthetic.main.item_note.view.ivExpand
import kotlinx.android.synthetic.main.item_note.view.tvDate
import kotlinx.android.synthetic.main.item_note.view.tvTitle
import kotlinx.android.synthetic.main.report_folded.view.tvCharacters
import kotlinx.android.synthetic.main.report_folded.view.tvSentences
import kotlinx.android.synthetic.main.report_folded.view.tvWords
import kotlinx.android.synthetic.main.report_unfolded.view.tvCharactersContent
import kotlinx.android.synthetic.main.report_unfolded.view.tvParagraphsContent
import kotlinx.android.synthetic.main.report_unfolded.view.tvSentencesContent
import kotlinx.android.synthetic.main.report_unfolded.view.tvSizeContent
import kotlinx.android.synthetic.main.report_unfolded.view.tvWordsContent
import wordtextcounter.details.main.R
import wordtextcounter.details.main.store.entities.Report

class NotesAdapter : Adapter<NotesAdapter.ViewHolder>() {

  private var reports: List<Report>? = null


  val clickRelay = BehaviorRelay.create<NotesAction>()


  fun setReports(reports: List<Report>) {
    this.reports = reports
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindTo(reports!![position])
  }

  override fun getItemCount(): Int {
    return reports?.size ?: 0
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))
  }

  inner class ViewHolder(itemView: View) : android.support.v7.widget.RecyclerView.ViewHolder(
      itemView) {

    private val tvTitle = itemView.tvTitle
    private val tvDate = itemView.tvDate
    private val tvCharacters = itemView.tvCharacters
    private val tvWords = itemView.tvWords
    private val tvSentences = itemView.tvSentences

    private val tvCharactersContent = itemView.tvCharactersContent
    private val tvWordsContent = itemView.tvWordsContent
    private val tvSentencesContent = itemView.tvSentencesContent
    private val tvParagraphsContent = itemView.tvParagraphsContent
    private val tvSizeContent = itemView.tvSizeContent
    private val foldingCell = itemView.foldingCell
    private val ivExpand = itemView.ivExpand
    private val ibShare = itemView.ibShare
    private val ibEdit = itemView.ibEdit

    private val avMoreToLess = AnimatedVectorDrawableCompat.create(itemView.context,
        R.drawable.avd_more_to_less)
    private val avLessToMore = AnimatedVectorDrawableCompat.create(itemView.context,
        R.drawable.avd_less_to_more)

    init {
      ivExpand.setOnClickListener {

        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

        if (foldingCell.isUnfolded) {
          ivExpand.setImageDrawable(avLessToMore)
          avLessToMore?.start()
        } else {
          ivExpand.setImageDrawable(avMoreToLess)
          avMoreToLess?.start()
        }

        foldingCell.toggle(false)
      }


      ibEdit.setOnClickListener {
        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        clickRelay.accept(Edit(adapterPosition))
      }

      ibShare.setOnClickListener {
        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        clickRelay.accept(Share(adapterPosition))
      }

    }

    fun bindTo(report: Report) {
      tvTitle.text = report.name
      tvDate.text = report.dataText

      tvCharacters.text = report.characters
      tvWords.text = report.words
      tvSentences.text = report.sentences

      tvCharactersContent.text = report.characters
      tvWordsContent.text = report.words
      tvSentencesContent.text = report.sentences
      tvParagraphsContent.text = report.paragraphs

      tvSizeContent.text = report.size
    }
  }
}