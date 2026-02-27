package com.rustharbor.harbornotes.data.di

import android.content.Context
import androidx.room.Room
import com.rustharbor.harbornotes.data.local.HarborNotesDatabase
import com.rustharbor.harbornotes.data.local.NoteDao
import com.rustharbor.harbornotes.data.repository.NoteRepository
import com.rustharbor.harbornotes.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHarborNotesDatabase(@ApplicationContext context: Context): HarborNotesDatabase {
        return Room.databaseBuilder(
            context,
            HarborNotesDatabase::class.java,
            "harbor_notes.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: HarborNotesDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }
}