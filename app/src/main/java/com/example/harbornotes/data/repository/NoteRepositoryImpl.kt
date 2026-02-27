package com.rustharbor.harbornotes.data.repository

import com.rustharbor.harbornotes.data.local.NoteDao
import com.rustharbor.harbornotes.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override fun getArchivedNotes(): Flow<List<Note>> = noteDao.getArchivedNotes()

    override fun getNoteById(noteId: String): Flow<Note?> = noteDao.getNoteById(noteId)

    override suspend fun insertOrUpdateNote(note: Note) = noteDao.insertOrUpdateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    override fun searchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query)
}