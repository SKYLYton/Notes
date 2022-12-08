package com.notes.ui.fragment.note

import android.content.Intent
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
class EditNoteFragment : BaseFragment<FragmentEditNoteBinding>(FragmentEditNoteBinding::inflate) {
    private val viewModel: EditNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isMainScreen && isLandscape) {
            findNavController().popBackStack()
            return
        }

        arguments?.let { bundle ->
            viewModel.noteModel =
                EditNoteFragmentArgs.fromBundle(bundle).noteModelKey ?: NoteModel()
        }

        viewModel.apply {
            subscribe(uiState) { handleUiState(it) }
            start()
        }

        runBinding {
            container = root

            name.setText(viewModel.noteModel.name)
            description.setText(viewModel.noteModel.text)

            description.isVisible = !name.text.isNullOrEmpty()

            appBar.setTitle(R.string.edit_note_fragment_title_edit)

            appBar.inflateMenu(R.menu.menu_note)

            if (isLandscape) {
                appBar.setNavigationIcon(R.drawable.ic_close)
            } else {
                appBar.setNavigationIcon(R.drawable.ic_back)
            }

            appBar.setOnMenuItemClickListener {
                shareNote(viewModel.noteModel)
                true
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

    private fun shareNote(note: NoteModel) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, note.name)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, note.toStringForShare())
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share)))
    }

    private fun processNote() {
        runBinding {
            viewModel.saveNote(
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