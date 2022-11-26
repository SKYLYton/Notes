package com.notes.ui.fragment.notes

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
class NotesViewModel @Inject constructor(
    private val interactor: NotesInteractor
) : BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseState<List<NoteModel>>>()
    val uiState = _uiState.asSharedFlow()

    override fun onStart() {
        update()
    }

    fun update() {
        interactor.fetchNotes().handleResult(_uiState)
    }

    fun deleteNotes(notes: List<NoteModel>) {
        interactor.deleteNotes(notes).handleResult(_uiState)
    }
}