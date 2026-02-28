package com.rustharbor.harbornotes.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("noteId")]
)
data class ChecklistItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val noteId: String,
    val content: String,
    val isChecked: Boolean = false,
    val position: Int
)
