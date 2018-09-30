package wordtextcounter.details.main.feature

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.core.util.set

abstract class AbstractExpandableAdapter<GVH : RecyclerView.ViewHolder, CVH : RecyclerView.ViewHolder>
  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val headerExpandTracker = SparseIntArray()
  val viewTypes: SparseArray<ViewType> = SparseArray()

  private var allExpandedUsed = false
  open val showAllExpanded = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == TYPE_HEADER) {
      val holder = createGroupViewHolder(parent, viewType)
      holder.itemView.setOnClickListener {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
          onHeaderClicked(holder.adapterPosition)
        }
      }
      holder
    } else {
      createChildViewHolder(parent, viewType)
    }
  }

  fun isGroupExpanded(groupPosition: Int) = headerExpandTracker[viewTypes[groupPosition].index] != 0

  @Suppress("UNCHECKED_CAST")
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewType = getItemViewType(position)
    val index = viewTypes[position].index
    val groupIndex = viewTypes[position].groupIndex
    if (viewType == TYPE_HEADER) {
      bindGroupViewHolder(holder as GVH, index)
    } else {
      bindChildViewHolder(holder as CVH, groupIndex, index)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return viewTypes[position].type
  }

  override fun getItemCount(): Int {
    var totalCount = 0
    viewTypes.clear()

    if (groupCount() > 0) {
      if (!allExpandedUsed) {
        allExpandedUsed = true
        if (showAllExpanded) {
          for (i in 0 until groupCount()) {
            headerExpandTracker[i] = 1
          }
        }
      }
    }

    for (i in 0 until groupCount()) {
      viewTypes.put(totalCount, ViewType(i, TYPE_HEADER))
      totalCount++
      val childCount = childCount(i)
      if (headerExpandTracker[i] != 0) {
        //Expanded
        for (j in 0 until childCount) {
          viewTypes.put(totalCount, ViewType(j, TYPE_CHILD, i))
          totalCount++
        }
      }
    }
    return totalCount
  }

  @CallSuper
  open fun onHeaderClicked(position: Int) {
    val index = viewTypes[position].index
    val childCount = childCount(index)
    if (headerExpandTracker[index] == 0) {
      //collapsed, expand now
      headerExpandTracker[index] = 1
      notifyItemRangeInserted(position + 1, childCount)
    } else {
      //expanded, collapse now
      headerExpandTracker[index] = 0
      notifyItemRangeRemoved(position + 1, childCount)
    }
    notifyItemChanged(position)
  }

  abstract fun createGroupViewHolder(parent: ViewGroup, viewType: Int): GVH

  abstract fun groupCount(): Int

  abstract fun childCount(groupPosition: Int): Int

  abstract fun createChildViewHolder(parent: ViewGroup, viewType: Int): CVH

  abstract fun bindGroupViewHolder(viewHolder: GVH, groupPosition: Int)

  abstract fun bindChildViewHolder(viewHolder: CVH, groupPosition: Int, childPosition: Int)
}

data class ViewType(val index: Int, val type: Int, val groupIndex: Int = -1)

const val TYPE_HEADER = 0
const val TYPE_CHILD = 1