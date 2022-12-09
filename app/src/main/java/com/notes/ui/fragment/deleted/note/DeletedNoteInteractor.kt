package com.notes.ui.fragment.deleted.note

import com.notes.db.repository.NoteRepository
import com.notes.ui.base.BaseInteractor
import javax.inject.Inject

/**
 * @author Fedotov Yakov
 */
class DeletedNoteInteractor @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseInteractor() {

}