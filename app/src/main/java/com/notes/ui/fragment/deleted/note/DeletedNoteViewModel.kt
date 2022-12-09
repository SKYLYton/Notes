package com.notes.ui.fragment.deleted.note

import com.notes.model.NoteModel
import com.notes.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
@HiltViewModel
class DeletedNoteViewModel @Inject constructor(
    private val interactor: DeletedNoteInteractor
) : BaseViewModel() {

    var noteModel: NoteModel = NoteModel()

    override fun onStart() {

    }
}