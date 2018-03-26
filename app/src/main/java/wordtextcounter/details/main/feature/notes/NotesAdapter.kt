package wordtextcounter.details.main.feature.notes

import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.foldingCell
import kotlinx.android.synthetic.main.item_note.view.tvDate
import kotlinx.android.synthetic.main.item_note.view.tvTitle
import kotlinx.android.synthetic.main.report_folded.view.tvCharacters
import kotlinx.android.synthetic.main.report_folded.view.tvWords
import kotlinx.android.synthetic.main.report_unfolded.view.tvParagraphsContent
import wordtextcounter.details.main.R
import wordtextcounter.details.main.store.entities.Report

class NotesAdapter : Adapter<NotesAdapter.ViewHolder>() {

  private var reports: List<Report>? = null


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
    private val tvParagraphs = itemView.tvParagraphsContent
    private val foldingCell = itemView.foldingCell

    init {
      tvTitle.setOnClickListener {
        foldingCell.toggle(false)
      }
    }

    fun bindTo(report: Report) {
      tvTitle.text = report.name
      tvDate.text = report.dateText
      tvCharacters.text = report.characters
      tvWords.text = report.words
      tvParagraphs.text = report.paragraphs
    }
  }
}