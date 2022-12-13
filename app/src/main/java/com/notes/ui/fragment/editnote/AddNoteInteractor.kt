package com.notes.ui.fragment.editnote

import com.notes.db.room.repository.NoteRepository
import com.notes.model.NoteModel
import com.notes.ui.base.BaseInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
class AddNoteInteractor @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseInteractor() {

    fun saveNote(note: NoteModel) = flow {
        noteRepository.update(note)
        emit(noteRepository.note(note.id ?: 0).first())
    }
}