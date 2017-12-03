package wordtextcounter.details.main.feature.input.report

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.input.InputViewModel.Report
import wordtextcounter.details.main.feature.input.report.ReportAdapter.ResultViewHolder

/**
 * Created by hirak on 03/12/17.
 */
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

    @BindView(R.id.tvValue) lateinit var tvValue: TextView
    @BindView(R.id.tvResultType) lateinit var tvResultType: TextView

    init {
      ButterKnife.bind(this, itemView)
    }

    fun bind(report: Report) {
      tvValue.text = report.value
      tvResultType.setText(report.reportType.characters)
    }
  }
}