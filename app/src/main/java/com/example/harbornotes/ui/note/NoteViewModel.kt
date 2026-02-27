package com.example.harbornotes.ui.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harbornotes.data.model.Note
import com.example.harbornotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
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

    private val noteId: String? = savedStateHandle.get("noteId")

    init {
        if (noteId != null) {
            viewModelScope.launch {
                noteRepository.getNoteById(noteId).collect {
                    _note.value = it
                    _title.value = it?.title ?: ""
                    _content.value = it?.content ?: ""
                }
            }
        }

        viewModelScope.launch {
            combine(title, content) {
                title, content -> Pair(title, content)
            }
            .debounce(500)
            .collect{ saveNote() }
        }
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onContentChange(newContent: String) {
        _content.value = newContent
    }

    fun saveNote() {
        viewModelScope.launch {
            val noteToSave = _note.value?.copy(
                title = _title.value,
                content = _content.value,
                updatedAt = System.currentTimeMillis()
            ) ?: Note(
                title = _title.value,
                content = _content.value
            )
            noteRepository.insertOrUpdateNote(noteToSave)
        }
    }
}