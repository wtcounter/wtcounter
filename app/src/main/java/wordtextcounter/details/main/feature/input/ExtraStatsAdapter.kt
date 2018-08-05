package wordtextcounter.details.main.feature.input

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_extra_stat.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.store.entities.Stat

class ExtraStatsAdapter : RecyclerView.Adapter<ExtraStatsAdapter.ViewHolder>() {

  private var stats: List<Stat> = mutableListOf()

  fun setStats(reports: List<Stat>) {
    this.stats = reports
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_extra_stat, parent, false))
  }

  override fun getItemCount(): Int {
    return stats.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindTo(stats[position])
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvName = itemView.tvStatName
    private val tvStatValue = itemView.tvStatValue

    fun bindTo(stat: Stat) {
      tvName.text = stat.name
      tvStatValue.text = stat.value
    }
  }
}