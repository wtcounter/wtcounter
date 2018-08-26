package wordtextcounter.details.main.util

import wordtextcounter.details.main.store.entities.Report

sealed class Event

object NoEvent : Event()

data class EditReport(val report: Report) : Event()
data class ReportText(val text: String) : Event()

