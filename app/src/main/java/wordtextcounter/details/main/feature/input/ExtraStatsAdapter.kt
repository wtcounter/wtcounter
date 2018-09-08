package wordtextcounter.details.main.feature.input

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
import kotlinx.android.synthetic.main.item_extra_stat_child.view.*
import kotlinx.android.synthetic.main.item_extra_stat_header.view.*
import wordtextcounter.details.main.R

class ExtraStatsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var statGroups: List<ExtraStatGroup> = mutableListOf()
  private val headerExpandTracker = SparseIntArray()
  private val viewTypes: SparseArray<ViewType> = SparseArray()

  fun setStats(statGroups: List<ExtraStatGroup>) {
    this.statGroups = statGroups
    for (i in 0 until statGroups.size) {
      headerExpandTracker[i] = 1
    }
  }

  override fun getItemViewType(position: Int): Int {
    return viewTypes[position].type
  }

  override fun getItemCount(): Int {
    var totalCount = 0
    viewTypes.clear()

    var collapsedCount = 0
    for (i in 0 until statGroups.size) {
      viewTypes.put(totalCount, ViewType(i, TYPE_HEADER))
      totalCount++
      val statGroup = statGroups[i]
      val childCount = statGroup.stats.size
      if (headerExpandTracker[i] != 0) {
        //Expanded
        for (j in 0 until childCount) {
          viewTypes.put(totalCount, ViewType(j, TYPE_CHILD, i))
          totalCount++
        }
      } else {
        //collapsed
        collapsedCount += childCount
      }
    }
    return totalCount
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == TYPE_HEADER) {
      HeaderViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.item_extra_stat_header, parent,
              false))
    } else {
      ChildViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.item_extra_stat_child, parent,
              false))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val index = viewTypes[position].index
    val groupIndex = viewTypes[position].groupIndex
    if (viewType == TYPE_HEADER) {
      (holder as HeaderViewHolder).bindTo(statGroups[index])
    } else {
      (holder as ChildViewHolder).bindTo(statGroups[groupIndex].stats[index])
    }
  }

  inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvExtraStatGroupName = view.tvExtraStatGroupName

    init {
      view.setOnClickListener {
        onHeaderClicked(adapterPosition)
      }
    }

    fun bindTo(statGroup: ExtraStatGroup) {
      tvExtraStatGroupName.text = statGroup.groupName
    }
  }

  inner class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvExtraStatName = view.tvExtraStatName
    private val tvExtraStatValue = view.tvExtraStatValue

    fun bindTo(extraStat: ExtraStat) {
      tvExtraStatName.text = extraStat.name
      tvExtraStatValue.text = extraStat.value
    }
  }

  fun onHeaderClicked(position: Int) {
    val index = viewTypes[position].index
    val clickedStatGroup = statGroups[index]
    val childCount = clickedStatGroup.stats.size
    if (headerExpandTracker[index] == 0) {
      //collapsed, expand now
      headerExpandTracker[index] = 1
      notifyItemRangeInserted(position + 1, childCount)
    } else {
      //expanded, collapse now
      headerExpandTracker[index] = 0
      notifyItemRangeRemoved(position + 1, childCount)
    }
  }

}

data class ViewType(val index: Int, val type: Int, val groupIndex: Int = -1)

const val TYPE_HEADER = 0
const val TYPE_CHILD = 1