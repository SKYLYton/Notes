package com.notes.ui.fragment.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.processphoenix.ProcessPhoenix
import com.notes.databinding.FragmentMenuBinding
import com.notes.ui.activity.MainActivity
import com.notes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    private val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBinding {

            progressView1.progress = 20f

            deletedNotes.setOnClickListener {
                navigateSafety(MenuFragmentDirections.actionMenuFragmentToDeletedNotesFragment())
            }

            aboutApp.setOnClickListener {
                navigateSafety(MenuFragmentDirections.actionMenuFragmentToAboutAppFragment())
            }

            logOut.setOnClickListener {
                viewModel.logOut()
                restartNavigation()
            }
        }
    }

    private fun restartNavigation() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        ProcessPhoenix.triggerRebirth(requireContext(), intent)
    }
}