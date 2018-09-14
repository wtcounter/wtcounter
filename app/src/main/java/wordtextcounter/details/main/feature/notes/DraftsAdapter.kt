package wordtextcounter.details.main.feature.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.AbstractExpandableAdapter
import wordtextcounter.details.main.store.data.DraftWithHistory

class DraftsAdapter(val draftsWithHistory: List<DraftWithHistory>) : AbstractExpandableAdapter<DraftsAdapter.DraftHolder, DraftsAdapter.HistoryHolder>() {
  override fun createGroupViewHolder(parent: ViewGroup, viewType: Int): DraftHolder {
    return DraftHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )
  }

  override fun groupCount(): Int {
    return draftsWithHistory.size
  }

  override fun childCount(groupPosition: Int): Int {
    return draftsWithHistory[groupPosition].draftHistories.size
  }

  override fun createChildViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
    return HistoryHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )
  }

  override fun bindGroupViewHolder(viewHolder: DraftHolder, groupPosition: Int) {
  }

  override fun bindChildViewHolder(viewHolder: HistoryHolder, groupPosition: Int, childPosition: Int) {
  }


  class DraftHolder(view : View) : RecyclerView.ViewHolder(view) {

  }

  class HistoryHolder(view : View) : RecyclerView.ViewHolder(view) {

  }
}