package com.rustharbor.harbornotes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rustharbor.harbornotes.data.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class HarborNotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}