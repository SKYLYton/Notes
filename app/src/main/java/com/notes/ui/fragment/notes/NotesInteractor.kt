package com.notes.ui.fragment.notes

import com.notes.db.room.repository.NoteRepository
import com.notes.model.NoteModel
import com.notes.ui.base.BaseInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
class NotesInteractor @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseInteractor() {

    fun fetchNotes() = noteRepository.notesNotDeleted()

    fun fetchNotesNotSent() = noteRepository.notes()

    fun fetchSentNotes() = flow {
        val notes = noteRepository.notes().first()
        emit(notes.filter { it.isSent })
    }

    fun fetchNote(noteId: Int) = noteRepository.note(noteId)

    fun saveNote(note: NoteModel) = flow {
        noteRepository.update(note)
        emit(noteRepository.lastNote().first())
    }

    fun deleteNotes(notes: List<NoteModel>) = flow {
        noteRepository.delete(notes)
        emit(noteRepository.notesNotDeleted().first())
    }

    fun deleteNote(note: NoteModel) = flow {
        noteRepository.delete(note)
        emit(noteRepository.notesNotDeleted().first())
    }
}