package wordtextcounter.details.main.feature.input.report

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_result.view.tvResultType
import kotlinx.android.synthetic.main.item_result.view.tvValue
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.input.InputViewModel.Report
import wordtextcounter.details.main.feature.input.report.ReportAdapter.ResultViewHolder

class ReportAdapter : Adapter<ResultViewHolder>() {

  private var reports: List<Report> = mutableListOf()


  fun setResults(results: List<Report>) {
    this.reports = results
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
    holder.bind(reports[position])
  }

  override fun getItemCount() = reports.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
    return ResultViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false))
  }

  class ResultViewHolder(itemView: View) : ViewHolder(itemView) {


    private val tvValue: TextView = itemView.tvValue
    private val tvResultType: TextView = itemView.tvResultType


    fun bind(report: Report) {
      tvValue.text = report.value
      tvResultType.setText(report.reportType.characters)
    }
  }
}