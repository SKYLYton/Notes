package com.notes.ui.fragment.deleted.notes

import com.notes.db.repository.NoteRepository
import com.notes.model.NoteModel
import com.notes.ui.base.BaseInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
class DeletedNotesInteractor @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseInteractor() {

    fun fetchNotes() = noteRepository.deletedNotes()

    fun fetchNote(noteId: Int) = noteRepository.note(noteId)

    fun deleteNotes(notes: List<NoteModel>) = flow {
        noteRepository.deleteFromDB(notes)
        emit(noteRepository.deletedNotes().first())
    }

    fun restoreNote(note: NoteModel) = flow {
        noteRepository.restore(note)
        emit(noteRepository.deletedNotes().first())
    }

    fun deleteNote(note: NoteModel) = flow {
        noteRepository.deleteFromDB(note)
        emit(noteRepository.deletedNotes().first())
    }
}