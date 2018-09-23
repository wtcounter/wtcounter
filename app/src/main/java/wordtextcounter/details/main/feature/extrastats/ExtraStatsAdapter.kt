package wordtextcounter.details.main.feature.extrastats

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_extra_stat_child.view.*
import kotlinx.android.synthetic.main.item_extra_stat_header.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.AbstractExpandableAdapter

class ExtraStatsAdapter : AbstractExpandableAdapter<ExtraStatsAdapter.HeaderViewHolder, ExtraStatsAdapter.ChildViewHolder>() {

  private var statGroups: List<ExtraStatGroup> = mutableListOf()
  lateinit var expandMore: Drawable
  lateinit var expandLess: Drawable

  override val showAllExpanded = true

  fun setStats(statGroups: List<ExtraStatGroup>) {
    this.statGroups = statGroups
    notifyDataSetChanged()
  }

  override fun createGroupViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
    return HeaderViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_extra_stat_header, parent,
            false))
  }

  override fun groupCount() = statGroups.size

  override fun childCount(groupPosition: Int) = statGroups[groupPosition].stats.size

  override fun createChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
    return ChildViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_extra_stat_child, parent,
            false))
  }

  override fun bindGroupViewHolder(viewHolder: HeaderViewHolder, groupPosition: Int) {
    viewHolder.bindTo(statGroups[groupPosition])
  }

  override fun bindChildViewHolder(viewHolder: ChildViewHolder, groupPosition: Int,
      childPosition: Int) {
    viewHolder.bindTo(statGroups[groupPosition].stats[childPosition])
  }

  inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvExtraStatGroupName = view.tvExtraStatGroupName

    fun bindTo(statGroup: ExtraStatGroup) {
      tvExtraStatGroupName.setText(statGroup.groupName)
      if (isGroupExpanded(adapterPosition)) {
        tvExtraStatGroupName.setCompoundDrawablesWithIntrinsicBounds(null, null, expandLess, null)
      } else {
        tvExtraStatGroupName.setCompoundDrawablesWithIntrinsicBounds(null, null, expandMore, null)
      }
    }
  }

  inner class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvExtraStatName = view.tvExtraStatName
    private val tvExtraStatValue = view.tvExtraStatValue

    fun bindTo(extraStat: ExtraStat) {
      tvExtraStatName.setText(extraStat.name)
      tvExtraStatValue.text = extraStat.value
    }
  }
}