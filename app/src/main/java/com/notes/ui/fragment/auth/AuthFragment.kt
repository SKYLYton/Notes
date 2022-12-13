package com.notes.ui.fragment.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.notes.databinding.FragmentAuthBinding
import com.notes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {
    override val isBottomNavVisible = false

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBinding {

            login.setOnClickListener {
                viewModel.auth(email.text.toString(), password.text.toString())
            }

            register.setOnClickListener {
                navigateSafety(AuthFragmentDirections.actionAuthFragmentToRegistrationFragment())
            }
        }

        viewModel.apply {
            subscribe(authState, ::handleAuthState)
            subscribe(error, ::handleErrorState)
            start()
        }
    }

    private fun handleAuthState(isSuccess: Boolean) {
        if (isSuccess) {
            navigateSafety(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
        }
    }

    private fun handleErrorState(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}

