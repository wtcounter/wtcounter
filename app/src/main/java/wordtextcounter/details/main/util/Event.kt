package wordtextcounter.details.main.util

import wordtextcounter.details.main.store.entities.Report

sealed class Event

object NoEvent : Event()

data class EditReport(val report: Report) : Event()
data class ExtraStatText(val text: String) : Event()
data class DeleteReport(val report: Report) : Event()
data class EditDraft(val text: String, val id: Long) : Event()
data class EditDraftHistory(val text: String, val id: Long, val parentDraftText : String) : Event()
data class ChangeDraftState(val text: String, val id: Long) : Event()
data class DeleteDraft(val id: Long) : Event()
data class ShareText(val shareText: String) : Event()
