package com.rustharbor.harbornotes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustharbor.harbornotes.data.model.ChecklistItem
import com.rustharbor.harbornotes.data.model.Note

@Database(entities = [Note::class, ChecklistItem::class], version = 2, exportSchema = false)
abstract class HarborNotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
