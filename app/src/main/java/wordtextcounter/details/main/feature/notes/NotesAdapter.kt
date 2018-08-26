package wordtextcounter.details.main.feature.notes

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils.getRelativeTimeSpanString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.synthetic.main.item_note.view.*
import kotlinx.android.synthetic.main.report_folded.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.store.entities.Report

class NotesAdapter : ListAdapter<Report, NotesAdapter.ViewHolder>(NotesDiffCallback()) {

  val clickRelay: PublishRelay<NotesAction> = PublishRelay.create()

  override fun onBindViewHolder(
      holder: ViewHolder,
      position: Int
  ) {
    holder.bindTo(getItem(position))
  }

  override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
  ): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )
  }

  inner class ViewHolder(itemView: View) : android.support.v7.widget.RecyclerView.ViewHolder(
      itemView
  ) {

    private val tvTitle = itemView.tvTitle
    private val tvDate = itemView.tvDate
    private val tvCharacters = itemView.tvCharacters
    private val tvWords = itemView.tvWords
    private val tvSentences = itemView.tvSentences
    private val tvParagraphs = itemView.tvParagraphs
    private val ibDelete = itemView.ibDelete
    private val ibEdit = itemView.ibEdit

    init {
      ibDelete.setOnClickListener {
        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        clickRelay.accept(Delete(adapterPosition))
      }
      ibEdit.setOnClickListener {
        if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        clickRelay.accept(Edit(adapterPosition))
      }
    }

    fun bindTo(report: Report) {
      tvTitle.text = report.name
      tvDate.text = report.time_added?.let {
        getRelativeTimeSpanString(it, System.currentTimeMillis(), 0)
      }

      tvCharacters.text = report.characters
      tvWords.text = report.words
      tvSentences.text = report.sentences
      tvParagraphs.text = report.paragraphs
    }
  }
}