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
import wordtextcounter.details.main.feature.input.InputViewModel.ReportMeta
import wordtextcounter.details.main.feature.input.report.ReportAdapter.ResultViewHolder

class ReportAdapter : Adapter<ResultViewHolder>() {

  private var reportMetas: List<ReportMeta> = mutableListOf()


  fun setResults(results: List<ReportMeta>) {
    this.reportMetas = results
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
    holder.bind(reportMetas[position])
  }

  override fun getItemCount() = reportMetas.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
    return ResultViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false))
  }

  class ResultViewHolder(itemView: View) : ViewHolder(itemView) {


    private val tvValue: TextView = itemView.tvValue
    private val tvResultType: TextView = itemView.tvResultType


    fun bind(reportMeta: ReportMeta) {
      tvValue.text = reportMeta.value
      tvResultType.setText(reportMeta.reportType.characters)
    }
  }
}