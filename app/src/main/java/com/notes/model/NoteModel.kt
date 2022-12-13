package com.notes.model

import android.os.Bundle
import android.os.Parcelable
import com.notes.db.room.entity.NoteEntity
import com.notes.extension.parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Fedotov Yakov
 */
@Parcelize
data class NoteModel(
    val id: Int?,
    val name: String,
    val text: String,
    val date: String,
    val isDeleted: Boolean,
    val firestoreId: String = "",
    val isSent: Boolean = false,
    var isCheck: Boolean = false
) : Parcelable {
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

    constructor() : this(null, "", "", "")

    fun equalsFromDB(noteDB: NoteModel) =
        this.run { noteDB.name == name && noteDB.text == text && noteDB.date == date }

    fun equalsFromFirestore(noteFirestore: NoteModel) =
        this.run { noteFirestore.name == name && noteFirestore.text == text && noteFirestore.date == date }

    fun clone(
        name: String = this.name,
        text: String = this.text,
        date: String = this.date,
        isSent: Boolean = this.isSent,
        firestoreId: String = this.firestoreId
    ): NoteModel {
        return NoteModel(
            id,
            name,
            text,
            date,
            isDeleted,
            isSent = isSent,
            firestoreId = firestoreId
        )
    }

    fun toStringForShare() = "$name\n\n$text\n\n$date"
}

val NoteEntity.toModel: NoteModel
    get() = NoteModel(id, name, text, date, isDelete, isSent = isSent, firestoreId = firestoreId)

val NoteModel.toEntity: NoteEntity
    get() = NoteEntity(id, name, text, date, isSent, firestoreId)

val NoteModel.toEntityWithoutId: NoteEntity
    get() = NoteEntity(name, text, date, isSent, firestoreId)

val NoteModel.toBundle: Bundle
    get() = Bundle().apply {
        putParcelable(NOTE_MODEL_KEY, this@toBundle)
    }

val Bundle.toNoteModel: NoteModel?
    get() = parcelable(NOTE_MODEL_KEY)

const val NOTE_MODEL_KEY = "NOTE_MODEL_KEY"