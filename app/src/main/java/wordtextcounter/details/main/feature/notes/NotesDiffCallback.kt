package wordtextcounter.details.main.feature.notes

import android.support.v7.util.DiffUtil
import wordtextcounter.details.main.store.entities.Report

class NotesDiffCallback : DiffUtil.ItemCallback<Report>() {
  override fun areItemsTheSame(oldItem: Report?, newItem: Report?): Boolean {
    return oldItem?.id == newItem?.id
  }

  override fun areContentsTheSame(oldItem: Report?, newItem: Report?): Boolean {
    return oldItem == newItem
  }
}