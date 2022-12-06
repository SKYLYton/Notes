package com.notes.ui.fragment.notes

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.notes.R
import com.notes.databinding.FragmentNotesBinding
import com.notes.extension.subscribeOnResult
import com.notes.model.NoteModel
import com.notes.model.UpdateModel
import com.notes.ui.base.BaseFragment
import com.notes.ui.fragment.empty.EmptyFragmentDirections
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

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavController()

        runBinding {
            notes.adapter = adapter
            adapter.onItemClickListener = { note ->
                navigateToEditNote(note)
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
                viewModel.addNote()
            }

            appBar.setSecondAppBarOnMenuItemClickListener = { item ->
                var hideSecondBar = true
                when (item.itemId) {
                    R.id.delete -> viewModel.deleteNotes(adapter.list.filter { it.isCheck })
                    R.id.check_all -> {
                        adapter.selectAll()
                        hideSecondBar = false
                    }
                }
                hideSecondBar
            }
        }

        addHandleBackCallBackActivity(callback)


        viewModel.apply {
            subscribe(uiState) { handleUiState(it) }
            subscribe(selectedNoteState) { handleSelectedNoteState(it) }
            subscribe(addNoteState) { handleAddNoteState(it) }
            start()
        }
    }

    private fun initNavController() {
        navController = if (isLandscape) {
            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.navController.apply {
                subscribeOnResult<UpdateModel>(
                    NOTES_UPDATE_KEY,
                    viewLifecycleOwner
                ) {
                    viewModel.update()
                }
            }
        } else {
            findNavController()
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

    private fun handleSelectedNoteState(note: NoteModel? = null) {
        navigateToEditNote(note)
    }

    private fun handleAddNoteState(note: NoteModel? = null) {
        navigateToAddNote(note)
    }

    private fun navigateToAddNote(note: NoteModel? = NoteModel()) {
        viewModel.selectedNoteId = note?.id
        if (isLandscape) {
            navigateToNote(EmptyFragmentDirections.actionEmptyFragmentToAddNoteFragment(note))
        } else {
            navigateToNote(NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(note))
        }
    }

    private fun navigateToEditNote(note: NoteModel? = null) {
        viewModel.selectedNoteId = note?.id
        if (isLandscape) {
            navigateToNote(EmptyFragmentDirections.actionEmptyFragmentToEditNoteFragment(note))
        } else {
            navigateToNote(NotesFragmentDirections.actionNotesFragmentToEditNoteFragment(note))
        }
    }

    private fun navigateToNote(destination: NavDirections) {
        callback.isEnabled = true
        if (isLandscape) {
            if (navController.currentDestination?.id != R.id.emptyFragment) {
                navController.popBackStack()
            }
            navController.currentDestination?.getAction(destination.actionId)?.let {
                navController.navigate(destination)
            }
        } else {
            navigateSafety(destination)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
        viewModel.loadSelectedId()
    }

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            isEnabled = false
            viewModel.selectedNoteId = null
            if (navController.currentDestination?.id != R.id.emptyFragment || navController.currentDestination?.id != R.id.notesFragment) {
                navController.popBackStack()
            }
        }
    }
}

const val NOTES_UPDATE_KEY = "NOTES_UPDATE_KEY"