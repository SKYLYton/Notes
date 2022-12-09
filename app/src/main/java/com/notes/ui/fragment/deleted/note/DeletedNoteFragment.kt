package com.notes.ui.fragment.deleted.note

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.notes.R
import com.notes.databinding.FragmentDeletedNoteBinding
import com.notes.model.NoteModel
import com.notes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class DeletedNoteFragment :
    BaseFragment<FragmentDeletedNoteBinding>(FragmentDeletedNoteBinding::inflate) {
    private val viewModel: DeletedNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isMainScreen && isLandscape) {
            findNavController().popBackStack()
            return
        }

        arguments?.let { bundle ->
            viewModel.noteModel =
                DeletedNoteFragmentArgs.fromBundle(bundle).noteModelKey ?: NoteModel()
        }

        viewModel.apply {
            start()
        }

        runBinding {
            container = root

            name.setText(viewModel.noteModel.name)
            description.setText(viewModel.noteModel.text)

            description.isVisible = !name.text.isNullOrEmpty()

            if (isLandscape) {
                appBar.setNavigationIcon(R.drawable.ic_close)
            } else {
                appBar.setNavigationIcon(R.drawable.ic_back)
            }

            appBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private val isMainScreen: Boolean
        get() = mainNavController?.currentDestination?.id == R.id.deletedNotesFragment

}