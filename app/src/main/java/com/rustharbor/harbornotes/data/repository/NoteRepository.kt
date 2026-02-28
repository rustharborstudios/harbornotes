package com.rustharbor.harbornotes.data.repository

import com.rustharbor.harbornotes.data.model.ChecklistItem
import com.rustharbor.harbornotes.data.model.Note
import com.rustharbor.harbornotes.data.model.NoteWithChecklist
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    fun getArchivedNotes(): Flow<List<Note>>

    fun getNoteById(noteId: String): Flow<Note?>

    suspend fun insertOrUpdateNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun searchNotes(query: String): Flow<List<Note>>

    // Checklist operations
    fun getNoteWithChecklist(noteId: String): Flow<NoteWithChecklist?>
    
    suspend fun insertChecklistItem(item: ChecklistItem)
    
    suspend fun updateChecklistItem(item: ChecklistItem)
    
    suspend fun deleteChecklistItem(item: ChecklistItem)
    
    fun getChecklistItems(noteId: String): Flow<List<ChecklistItem>>
}
