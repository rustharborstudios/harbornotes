package com.rustharbor.harbornotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustharbor.harbornotes.data.model.Note
import com.rustharbor.harbornotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val notes = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                noteRepository.getAllNotes()
            } else {
                noteRepository.searchNotes(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var lastDeletedNote: Note? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            noteRepository.insertOrUpdateNote(note.copy(isPinned = !note.isPinned))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            lastDeletedNote = note
            noteRepository.deleteNote(note)
        }
    }

    fun undoDelete() {
        lastDeletedNote?.let { note ->
            viewModelScope.launch {
                noteRepository.insertOrUpdateNote(note)
                lastDeletedNote = null
            }
        }
    }

    fun duplicateNote(note: Note) {
        viewModelScope.launch {
            val duplicatedNote = note.copy(
                id = UUID.randomUUID().toString(),
                title = "${note.title} (Copy)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                isPinned = false
            )
            noteRepository.insertOrUpdateNote(duplicatedNote)
        }
    }
}