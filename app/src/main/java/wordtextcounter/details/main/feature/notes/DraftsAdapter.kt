package wordtextcounter.details.main.feature.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_draft.view.*
import kotlinx.android.synthetic.main.item_draft_history.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.AbstractExpandableAdapter
import wordtextcounter.details.main.store.data.DraftData
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.DraftHistory

class DraftsAdapter(val draftsWithHistory: List<DraftWithHistory>) : AbstractExpandableAdapter<DraftsAdapter.DraftHolder, DraftsAdapter.HistoryHolder>() {
  override fun createGroupViewHolder(parent: ViewGroup, viewType: Int): DraftHolder {
    return DraftHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_draft, parent, false)
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_draft_history, parent, false)
    )
  }

  override fun bindGroupViewHolder(viewHolder: DraftHolder, groupPosition: Int) {
    val draft = draftsWithHistory[groupPosition]
    viewHolder.bind(draft.draft.draftData)
  }

  override fun bindChildViewHolder(viewHolder: HistoryHolder, groupPosition: Int, childPosition: Int) {
    val draft = draftsWithHistory[groupPosition]
    val draftHistory = draft.draftHistories[childPosition]
    viewHolder.bind(draftHistory)
  }


  inner class DraftHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val text = itemView.tvText

    fun bind(draftData: DraftData) {
      text.text = draftData.text
    }
  }

  inner class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val historyText = itemView.tvHistoryText
    fun bind(draftHistory: DraftHistory) {
      historyText.text = draftHistory.text
    }
  }
}