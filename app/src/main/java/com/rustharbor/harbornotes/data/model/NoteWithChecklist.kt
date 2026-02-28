package com.rustharbor.harbornotes.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithChecklist(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val checklistItems: List<ChecklistItem>
)
