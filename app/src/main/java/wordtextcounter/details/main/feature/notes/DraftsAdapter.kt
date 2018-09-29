package wordtextcounter.details.main.feature.notes

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.synthetic.main.item_draft.view.*
import kotlinx.android.synthetic.main.item_draft_history.view.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.AbstractExpandableAdapter
import wordtextcounter.details.main.store.data.DraftWithHistory
import wordtextcounter.details.main.store.entities.Draft
import wordtextcounter.details.main.store.entities.DraftHistory

class DraftsAdapter :
    AbstractExpandableAdapter<DraftsAdapter.DraftHolder, DraftsAdapter.HistoryHolder>() {
  val clickRelay: PublishRelay<DraftActions> = PublishRelay.create<DraftActions>()
  val drafts : MutableList<DraftWithHistory> = mutableListOf()
  override fun createGroupViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): DraftHolder {
    return DraftHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_draft, parent, false)
    )
  }

  override fun groupCount(): Int {
    return drafts.size
  }

  override fun childCount(groupPosition: Int): Int {
    return drafts[groupPosition].draftHistories.size
  }

  override fun createChildViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): HistoryHolder {
    return HistoryHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_draft_history, parent, false)
    )
  }

  override fun bindGroupViewHolder(
    viewHolder: DraftHolder,
    groupPosition: Int
  ) {
    val draft = drafts[groupPosition]
    viewHolder.bind(draft.draft)
  }

  override fun bindChildViewHolder(
    viewHolder: HistoryHolder,
    groupPosition: Int,
    childPosition: Int
  ) {
    val draft = drafts[groupPosition]
    val draftHistory = draft.draftHistories[childPosition]
    viewHolder.bind(
        draftHistory, draft ,childPosition == 0, childPosition == childCount(groupPosition) - 1
    )
  }

  inner class DraftHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val text = itemView.tvText
    private val edit = itemView.ibEdit
    private val delete = itemView.ibDelete
    private val ibExpand = itemView.ibExpand

    init {
      edit.setOnClickListener {
        clickRelay.accept(DraftActions.DraftEdit(itemView.tag as Draft))
      }

      delete.setOnClickListener {
        clickRelay.accept(DraftActions.DraftDelete(itemView.tag as Draft))
      }

      ibExpand.setOnClickListener {
        
      }
    }

    fun bind(draft: Draft) {
      text.text = draft.draftData.text
      itemView.tag = draft
    }
  }

  fun dispatchUpdates(data: List<DraftWithHistory>) {
    drafts.clear()
    drafts.addAll(data)
    notifyDataSetChanged()
  }

  inner class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val historyText = itemView.tvHistoryText
    private val timeStampText = itemView.tvTimeStamp
    private val topLine = itemView.lineIndicatorTop
    private val bottomLine = itemView.lineIndicatorBottom
    private val editHistory = itemView.ibEditHistory
    private val deleteHistory = itemView.ibDeleteHistory

    init {
      editHistory.setOnClickListener {
        clickRelay.accept(DraftActions.DraftHistoryEdit(itemView.tag as DraftHistory, itemView.getTag(R.id.parent_draft) as DraftWithHistory))
      }

      deleteHistory.setOnClickListener {
        clickRelay.accept(DraftActions.DraftHistoryDelete(itemView.tag as DraftHistory))
      }
    }

    fun bind(
      draftHistory: DraftHistory,
      parentDraft: DraftWithHistory,
      isFirst: Boolean,
      isLast: Boolean
    ) {
      historyText.text = draftHistory.text
      if (isFirst) {
        topLine.visibility = View.INVISIBLE
      } else {
        topLine.visibility = View.VISIBLE
      }

      if (isLast) {
        bottomLine.visibility = View.INVISIBLE
      } else {
        bottomLine.visibility = View.VISIBLE
      }

      timeStampText.text = getRelativeDateTimeString(
          itemView.context, draftHistory.createdAt, MINUTE_IN_MILLIS, DAY_IN_MILLIS,
          FORMAT_ABBREV_RELATIVE
      )
      itemView.setTag(R.id.parent_draft, parentDraft)
      itemView.tag = draftHistory
    }
  }

  sealed class DraftActions {
    data class DraftEdit(val draft: Draft) : DraftActions()
    data class DraftDelete(val draft: Draft) : DraftActions()
    data class DraftHistoryEdit(val draftHistory: DraftHistory, val parentDraft: DraftWithHistory) : DraftActions()
    data class DraftHistoryDelete(val draftHistory: DraftHistory) : DraftActions()
  }
}