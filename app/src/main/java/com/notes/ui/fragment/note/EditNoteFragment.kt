package com.notes.ui.fragment.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.notes.R
import com.notes.databinding.FragmentEditNoteBinding
import com.notes.extension.getTimeToString
import com.notes.model.NoteModel
import com.notes.model.toNoteModel
import com.notes.ui.base.BaseFragment
import com.notes.ui.state.BaseState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class EditNoteFragment : BaseFragment<FragmentEditNoteBinding>(FragmentEditNoteBinding::inflate) {
    private val viewModel: EditNoteViewModel by viewModels()

    private var model: NoteModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            model = bundle.toNoteModel
        }

        viewModel.apply {
            subscribe(uiState) { handleUiState(it) }
            start()
        }

        runBinding {
            model?.let { note ->
                name.setText(note.name)
                text.setText(note.text)
                appBar.setTitle(R.string.edit_note_fragment_title_edit)
            }

            if (model != null) {
                appBar.setFirstMenu(R.menu.menu_edit)
            } else {
                appBar.setFirstMenu(R.menu.menu_default)
            }

            appBar.setAppBarOnMenuItemClickListener = { item ->
                when (item.itemId) {
                    R.id.add -> addNote()
                    R.id.save -> saveNote()
                }
            }
        }
    }

    private fun addNote() {
        runBinding {
            if (name.text.isNotEmpty()) {
                viewModel.addNewNote(
                    NoteModel(
                        name.text.toString(),
                        text.text.toString(),
                        Date().getTimeToString()
                    )
                )
            }
        }
    }

    private fun saveNote() {
        runBinding {
            if (name.text.isNotEmpty()) {
                viewModel.saveNote(
                    NoteModel(
                        model?.id ?: 0,
                        name.text.toString(),
                        text.text.toString(),
                        Date().getTimeToString()
                    )
                )
            }
        }
    }

    private fun handleUiState(state: BaseState<Boolean>) {
        when (state) {
            is BaseState.Error -> {

            }
            is BaseState.Loading -> {

            }
            is BaseState.Success -> {
                // TODO: Вывести ошибку, если не success
                if (state.item) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}