package com.notes.ui.activity

import android.os.Bundle
import com.notes.R

/**
 * @author Fedotov Yakov
 */
interface ActivityRouter {
    fun navigationTo(screen: Screen, bundle: Bundle? = null)
}

enum class Screen(val id: Int) {
    Notes(R.id.notesFragment), EditNote(R.id.editNoteFragment)
}