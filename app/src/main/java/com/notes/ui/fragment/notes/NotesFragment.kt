package com.notes.ui.fragment.notes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.notes.R
import com.notes.databinding.FragmentNotesBinding
import com.notes.model.NoteModel
import com.notes.model.toBundle
import com.notes.ui.activity.Screen
import com.notes.ui.base.BaseFragment
import com.notes.ui.fragment.notes.adapter.NotesAdapter
import com.notes.ui.state.BaseState
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class NotesFragment : BaseFragment<FragmentNotesBinding>(FragmentNotesBinding::inflate) {

    private val viewModel: NotesViewModel by viewModels()

    private val adapter = NotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            subscribe(uiState) { handleUiState(it) }
            start()
        }

        runBinding {
            notes.adapter = adapter
            adapter.onItemClickListener = { note ->
                navigateTo(Screen.EditNote, note.toBundle)
            }
            adapter.onItemSelectedListener = { note, count ->
                appBar.visibleSecondMenu(count > 0)
                if (count > 0) {
                    appBar.setSecondTitle(getString(R.string.notes_fragment_title_count, count))
                }
            }
            appBar.navigationOnClickListener = {
                adapter.unSelectAll()
            }

            appBar.setAppBarOnMenuItemClickListener = {
                navigateTo(Screen.EditNote)
            }

            appBar.setSecondAppBarOnMenuItemClickListener = {
                viewModel.deleteNotes(adapter.list.filter { it.isCheck })
                adapter.unSelectAll()
                true
            }
        }

    }

    private fun handleUiState(state: BaseState<List<NoteModel>>) {
        when (state) {
            is BaseState.Error -> {

            }
            is BaseState.Loading -> {

            }
            is BaseState.Success -> {
                adapter.submitList(state.item)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.update()
    }
}