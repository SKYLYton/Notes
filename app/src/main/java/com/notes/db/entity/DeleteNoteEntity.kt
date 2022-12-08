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
    tableName = "delete_note"
)
data class DeleteNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val text: String,
    val date: String
) : BaseEntity() {
    constructor(name: String, text: String, date: String) : this(null, name, text, date)
}

val NoteEntity.toDeleteEntity: DeleteNoteEntity
    get() = DeleteNoteEntity(
        id, name, text, date
    )