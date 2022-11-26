package com.notes.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vpodarok.db.entity.BaseEntity

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
    val date: String
) : BaseEntity() {
    constructor(name: String, text: String, date: String) : this(null, name, text, date)
}