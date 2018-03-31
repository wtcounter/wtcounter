package wordtextcounter.details.main.util

import wordtextcounter.details.main.store.entities.Report

sealed class Event

data class EditReport(val report: Report) : Event()

