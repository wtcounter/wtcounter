package wordtextcounter.details.main.feature.notes

import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.tvCharacters
import kotlinx.android.synthetic.main.item_note.view.tvDate
import kotlinx.android.synthetic.main.item_note.view.tvParagraphs
import kotlinx.android.synthetic.main.item_note.view.tvTitle
import kotlinx.android.synthetic.main.item_note.view.tvWords
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
    private val tvParagraphs = itemView.tvParagraphs

    fun bindTo(report: Report) {
      tvTitle.text = report.name
      tvDate.text = report.dateText
      tvCharacters.text = report.characters
      tvWords.text = report.words
      tvParagraphs.text = report.paragraphs
    }

  }
}