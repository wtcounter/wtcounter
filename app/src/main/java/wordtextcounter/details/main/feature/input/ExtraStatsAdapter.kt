package wordtextcounter.details.main.feature.input

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class ExtraStatsAdapter : RecyclerView.Adapter<ExtraStatsAdapter.ViewHolder>() {

  private var statGroups: List<ExtraStatGroup> = mutableListOf()

  fun setStats(statGroups: List<ExtraStatGroup>) {
    this.statGroups = statGroups
  }

  override fun getItemViewType(position: Int): Int {
    return super.getItemViewType(position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getItemCount(): Int {
    var totalCount = 0
    statGroups.forEach {
      totalCount += it.stats.size
    }
    return totalCount + statGroups.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
  }

  inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

  }

}