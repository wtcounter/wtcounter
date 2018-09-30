package wordtextcounter.details.main.feature.notes

sealed class NotesAction

data class Edit(val position: Int) : NotesAction()
data class MoreStats(val position: Int) : NotesAction()
data class Delete(val position: Int) : NotesAction()