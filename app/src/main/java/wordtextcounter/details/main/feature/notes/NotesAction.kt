package wordtextcounter.details.main.feature.notes

sealed class NotesAction

data class Edit(val position: Int) : NotesAction()
data class Share(val position: Int) : NotesAction()