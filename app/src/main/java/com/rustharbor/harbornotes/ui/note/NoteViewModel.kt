package com.rustharbor.harbornotes.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustharbor.harbornotes.data.model.ChecklistItem
import com.rustharbor.harbornotes.data.model.Note
import com.rustharbor.harbornotes.data.model.NoteType
import com.rustharbor.harbornotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note = _note.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _type = MutableStateFlow(NoteType.TEXT)
    val type = _type.asStateFlow()

    private val _checklistItems = MutableStateFlow<List<ChecklistItem>>(emptyList())
    val checklistItems = _checklistItems.asStateFlow()

    private val noteId: String? = savedStateHandle.get("noteId")

    init {
        viewModelScope.launch {
            if (noteId != null) {
                val initialNote = noteRepository.getNoteById(noteId).filterNotNull().first()
                _note.value = initialNote
                _title.value = initialNote.title
                _type.value = initialNote.type
                
                if (initialNote.type == NoteType.CHECKLIST) {
                    _checklistItems.value = noteRepository.getChecklistItems(initialNote.id).first()
                } else {
                    _content.value = initialNote.content
                }
            }

            // Debounced auto-save
            combine(_title, _content, _checklistItems, _type) { t, c, cl, ty -> 
                Triple(t, c, cl) to ty 
            }
                .debounce(1000)
                .collect { (data, ty) ->
                    saveNoteInternal(_title.value, _content.value, _checklistItems.value, _type.value)
                }
        }
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onContentChange(newContent: String) {
        _content.value = newContent
    }

    fun toggleNoteType() {
        val newType = if (_type.value == NoteType.TEXT) NoteType.CHECKLIST else NoteType.TEXT
        _type.value = newType
        
        if (newType == NoteType.CHECKLIST && _checklistItems.value.isEmpty()) {
            addChecklistItem()
        }
    }

    fun addChecklistItem() {
        val currentNoteId = _note.value?.id ?: java.util.UUID.randomUUID().toString()
        val newItem = ChecklistItem(
            noteId = currentNoteId,
            content = "",
            position = _checklistItems.value.size
        )
        _checklistItems.value = _checklistItems.value + newItem
    }

    fun onChecklistItemChange(item: ChecklistItem, newContent: String) {
        _checklistItems.value = _checklistItems.value.map {
            if (it.id == item.id) it.copy(content = newContent) else it
        }
    }

    fun onChecklistItemCheckedChange(item: ChecklistItem, isChecked: Boolean) {
        _checklistItems.value = _checklistItems.value.map {
            if (it.id == item.id) it.copy(isChecked = isChecked) else it
        }
    }

    fun removeChecklistItem(item: ChecklistItem) {
        _checklistItems.value = _checklistItems.value - item
    }

    fun saveNote() {
        saveNoteInternal(_title.value, _content.value, _checklistItems.value, _type.value)
    }

    private fun saveNoteInternal(title: String, content: String, checklist: List<ChecklistItem>, type: NoteType) {
        if (title.isBlank() && content.isBlank() && checklist.isEmpty()) return

        viewModelScope.launch {
            val currentNote = _note.value
            val noteToSave = if (currentNote != null) {
                currentNote.copy(
                    title = title,
                    content = content,
                    type = type,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                Note(
                    title = title,
                    content = content,
                    type = type
                )
            }
            
            noteRepository.insertOrUpdateNote(noteToSave)
            
            if (type == NoteType.CHECKLIST) {
                checklist.forEach { item ->
                    // Ensure the item has the correct noteId if it was just created
                    val itemToSave = if (item.noteId != noteToSave.id) item.copy(noteId = noteToSave.id) else item
                    noteRepository.insertChecklistItem(itemToSave)
                }
            }

            if (_note.value == null || _note.value?.id != noteToSave.id) {
                _note.value = noteToSave
            }
        }
    }
}
