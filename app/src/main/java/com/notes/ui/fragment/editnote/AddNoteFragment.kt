package com.notes.ui.fragment.editnote

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.notes.R
import com.notes.databinding.FragmentEditNoteBinding
import com.notes.extension.getTimeToString
import com.notes.extension.sendResult
import com.notes.model.NoteModel
import com.notes.model.UpdateModel
import com.notes.ui.base.BaseFragment
import com.notes.ui.fragment.notes.NOTES_UPDATE_KEY
import com.notes.ui.state.BaseState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class AddNoteFragment : BaseFragment<FragmentEditNoteBinding>(FragmentEditNoteBinding::inflate) {
    private val viewModel: AddNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isMainScreen && isLandscape) {
            findNavController().popBackStack()
            return
        }

        arguments?.let { bundle ->
            viewModel.noteModel = AddNoteFragmentArgs.fromBundle(bundle).noteModelKey ?: NoteModel()
        }

        viewModel.apply {
            subscribe(uiState) { handleUiState(it) }
            start()
        }

        runBinding {
            container = root
            name.setText(viewModel.noteModel.name)
            description.setText(viewModel.noteModel.text)

            appBar.setTitle(R.string.edit_note_fragment_title_new)

            if (isLandscape) {
                appBar.setNavigationIcon(R.drawable.ic_close)
            } else {
                appBar.setNavigationIcon(R.drawable.ic_back)
            }

            appBar.setNavigationOnClickListener {
                onBackPressed()
            }
            name.doOnTextChanged { text, _, _, _ ->
                runWithAnim()
                description.isVisible = !text.isNullOrEmpty()
                processNote()
            }

            description.doOnTextChanged { _, _, _, _ ->
                processNote()
            }
        }
    }

    private fun processNote() {
        runBinding {
            viewModel.processNote(
                NoteModel(
                    name.text.toString(),
                    description.text.toString(),
                    Date().getTimeToString()
                )
            )
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
                    findNavController().sendResult(NOTES_UPDATE_KEY, UpdateModel)
                }
            }
        }
    }

    override fun onDestroyView() {
        findNavController().sendResult(NOTES_UPDATE_KEY, UpdateModel)
        super.onDestroyView()
    }
}