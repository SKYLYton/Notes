package com.notes.model

import android.os.Bundle
import android.os.Parcelable
import com.notes.db.entity.NoteEntity
import com.notes.extension.parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Fedotov Yakov
 */
@Parcelize
data class NoteModel(
    val id: Int?, val name: String, val text: String, val date: String, var isCheck: Boolean
): Parcelable {
    constructor(name: String, text: String, date: String) : this(
        null,
        name,
        text,
        date,
        false
    )

    constructor(id: Int?, name: String, text: String, date: String) : this(
        id,
        name,
        text,
        date,
        false
    )

    fun equalsFromDB(noteDB: NoteModel) =
        this.run { noteDB.name == name && noteDB.text == text && noteDB.date == date }
}

val NoteEntity.toModel: NoteModel
    get() = NoteModel(id, name, text, date)

val NoteModel.toEntity: NoteEntity
    get() = NoteEntity(id, name, text, date)

val NoteModel.toEntityWithoutId: NoteEntity
    get() = NoteEntity(name, text, date)

val NoteModel.toBundle: Bundle
    get() = Bundle().apply {
        putParcelable(NOTE_MODEL_KEY, this@toBundle)
    }

val Bundle.toNoteModel: NoteModel?
    get() = parcelable(NOTE_MODEL_KEY)

const val NOTE_MODEL_KEY = "NOTE_MODEL_KEY"