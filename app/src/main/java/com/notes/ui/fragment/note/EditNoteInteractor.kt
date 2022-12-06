package com.notes.ui.fragment.note

import com.notes.db.repository.NoteRepository
import com.notes.model.NoteModel
import com.notes.ui.base.BaseInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
class EditNoteInteractor @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseInteractor() {

    fun addNewNote(note: NoteModel) = flow {
        noteRepository.add(note)
        emit(noteRepository.lastNote().first())
    }

    fun saveNote(note: NoteModel) = flow {
        noteRepository.update(note)
        emit(noteRepository.note(note.id ?: 0).first())
    }
}