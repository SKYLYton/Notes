package com.notes.ui.fragment.note

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
class EditNoteViewModel @Inject constructor(
    private val interactor: EditNoteInteractor
) : BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseState<Boolean>>()
    val uiState = _uiState.asSharedFlow()

    var noteModel: NoteModel = NoteModel()

    override fun onStart() {
        if (noteModel.id == null) {
            interactor.saveNote(noteModel).handleResult(onSuccess = { noteDB ->
                noteModel = noteDB
            })
        }
    }

    fun saveNote(note: NoteModel) {
        noteModel = noteModel.clone(note.name, note.text, note.date)
        interactor.saveNote(noteModel).handleResult(onSuccess = { noteDB ->
            emit(_uiState, BaseState.Success(note.equalsFromDB(noteDB)))
        })
    }
}