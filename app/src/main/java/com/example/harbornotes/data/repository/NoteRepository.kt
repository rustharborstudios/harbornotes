package com.example.harbornotes.data.repository

import com.example.harbornotes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    fun getArchivedNotes(): Flow<List<Note>>

    fun getNoteById(noteId: String): Flow<Note?>

    suspend fun insertOrUpdateNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun searchNotes(query: String): Flow<List<Note>>
}