package com.notes.db.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Моделька БД заметка
 *
 * @author Fedotov Yakov
 */
@Entity(
    tableName = "note"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val text: String,
    val date: String,
    val isSent: Boolean,
    val firestoreId: String,
    var isDelete: Boolean = false
) : BaseEntity() {
    constructor(
        name: String,
        text: String,
        date: String,
        isSent: Boolean,
        firestoreId: String
    ) : this(
        null,
        name,
        text,
        date,
        isSent,
        firestoreId
    )
}