package com.rustharbor.harbornotes.data.repository

import com.rustharbor.harbornotes.data.local.NoteDao
import com.rustharbor.harbornotes.data.model.ChecklistItem
import com.rustharbor.harbornotes.data.model.Note
import com.rustharbor.harbornotes.data.model.NoteWithChecklist
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

    override fun getNoteWithChecklist(noteId: String): Flow<NoteWithChecklist?> =
        noteDao.getNoteWithChecklist(noteId)

    override suspend fun insertChecklistItem(item: ChecklistItem) =
        noteDao.insertChecklistItem(item)

    override suspend fun updateChecklistItem(item: ChecklistItem) =
        noteDao.updateChecklistItem(item)

    override suspend fun deleteChecklistItem(item: ChecklistItem) =
        noteDao.deleteChecklistItem(item)

    override fun getChecklistItems(noteId: String): Flow<List<ChecklistItem>> =
        noteDao.getChecklistItems(noteId)
}
