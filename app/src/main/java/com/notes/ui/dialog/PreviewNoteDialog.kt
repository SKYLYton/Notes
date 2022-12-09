package com.notes.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.notes.databinding.DialogPreviewNoteBinding
import com.notes.model.NoteModel
import com.notes.model.toBundle
import com.notes.model.toNoteModel
import com.notes.ui.activity.MainActivity
import com.notes.ui.base.BaseDialogFragment


/**
 * @author Fedotov Yakov
 */
class PreviewNoteDialog :
    BaseDialogFragment<DialogPreviewNoteBinding>(DialogPreviewNoteBinding::inflate) {

    private lateinit var noteModel: NoteModel

    override var dismissAnim: (() -> Unit)? = {
        startAnimClose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteModel = arguments?.toNoteModel ?: NoteModel()

        runBinding {
            name.text = noteModel.name
            description.text = noteModel.text
            this@PreviewNoteDialog.container = container

            note.setOnClickListener {
                onActionPerformed(
                    Bundle(arguments).apply {
                        putInt(RESULT_TYPE, ResultType.CLICK.ordinal)
                    }
                )
                dismiss()
            }

            select.setOnClickListener {
                onActionPerformed(
                    Bundle(arguments).apply {
                        putInt(RESULT_TYPE, ResultType.SELECT.ordinal)
                    }
                )
                dismissWithAnim()
            }

            unselect.setOnClickListener {
                onActionPerformed(
                    Bundle(arguments).apply {
                        putInt(RESULT_TYPE, ResultType.UNSELECT.ordinal)
                    }
                )
                dismissWithAnim()
            }

            restore.setOnClickListener {
                onActionPerformed(
                    Bundle(arguments).apply {
                        putInt(RESULT_TYPE, ResultType.RESTORE.ordinal)
                    }
                )
                dismissWithAnim()
            }

            delete.setOnClickListener {
                onActionPerformed(
                    Bundle(arguments).apply {
                        putInt(RESULT_TYPE, ResultType.DELETE.ordinal)
                    }
                )
                dismissWithAnim()
            }
            startAnimOpen()
        }
    }

    private fun startAnimOpen() {
        runBinding {
            note.post {
                runWithAnim()
                val lp = note.layoutParams as ConstraintLayout.LayoutParams
                if ((activity as MainActivity).isLandscape) {
                    lp.matchConstraintPercentHeight = 0.8.toFloat()
                } else {
                    lp.matchConstraintPercentHeight = 0.6.toFloat()
                }
                note.layoutParams = lp
                name.isVisible = true
                description.isVisible = true

            }

            menu.post {
                runWithAnim()
                val lp = menu.layoutParams as ConstraintLayout.LayoutParams
                lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                menu.layoutParams = lp
                select.isVisible = !noteModel.isCheck
                unselect.isVisible = noteModel.isCheck
                restore.isVisible = noteModel.isDeleted
                delete.isVisible = true
            }
        }
    }

    private fun startAnimClose() {
        runBindingWithAnim({
            dismissWithAnim()
        }) {
            val lpNote = note.layoutParams as ConstraintLayout.LayoutParams
            lpNote.matchConstraintPercentHeight = 0.toFloat()

            note.layoutParams = lpNote
            name.isVisible = false
            description.isVisible = false

            val lpMenu = menu.layoutParams as ConstraintLayout.LayoutParams
            lpMenu.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            lpNote.matchConstraintPercentHeight = 0.toFloat()
            menu.layoutParams = lpMenu
            select.isVisible = false
            unselect.isVisible = false
            restore.isVisible = false
            delete.isVisible = false
        }
    }

    companion object {
        fun newInstance(noteModel: NoteModel) =
            PreviewNoteDialog().apply {
                arguments = noteModel.toBundle
            }
    }
}

enum class ResultType {
    SELECT, UNSELECT, RESTORE, DELETE, CLICK
}

const val RESULT_TYPE = "RESULT_TYPE"

