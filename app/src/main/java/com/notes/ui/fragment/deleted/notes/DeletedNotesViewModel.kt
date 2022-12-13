package com.notes.ui.fragment.deleted.notes

import com.notes.db.firestore.FirestoreManager
import com.notes.model.NoteModel
import com.notes.ui.base.BaseViewModel
import com.notes.ui.state.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class DeletedNotesViewModel @Inject constructor(
    private val interactor: DeletedNotesInteractor,
    private val firestoreManager: FirestoreManager
) : BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseState<List<NoteModel>>>()
    val uiState = _uiState.asSharedFlow()

    private val _selectedNoteState = MutableSharedFlow<NoteModel>()
    val selectedNoteState = _selectedNoteState.asSharedFlow()

    var selectedNoteId: Int? = null

    var textQuery: String? = ""

    override fun onStart() {
        update()
    }

    fun loadSelectedId() {
        selectedNoteId?.let {
            interactor.fetchNote(it).handleResult(onSuccess = { note ->
                emit(_selectedNoteState, note)
            })
        }
    }

    fun update() {
        interactor.fetchNotesNotSent().handleResult(onSuccess = { notes ->
            firestoreManager.updateNotes(notes)
        })
        interactor.fetchNotes().handleResult(onSuccess = { notes ->
            if (textQuery.isNullOrEmpty()) {
                emit(_uiState, BaseState.Success(notes))
            } else {
                emit(
                    _uiState,
                    BaseState.Success(notes.filter {
                        it.name.contains(textQuery!!) || it.text.contains(textQuery!!)
                    })
                )
            }
        })
    }

    fun deleteNotes(notes: List<NoteModel>) {
        interactor.deleteNotes(notes).handleResult(_uiState)
    }

    fun deleteNote(note: NoteModel) {
        interactor.deleteNote(note).handleResult(_uiState)
    }

    fun restoreNote(note: NoteModel) {
        interactor.restoreNote(note).handleResult(_uiState)
    }
}