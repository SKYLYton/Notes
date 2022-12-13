package com.notes.ui.fragment.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.notes.R
import com.notes.databinding.FragmentRegistrationBinding
import com.notes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Fedotov Yakov
 */
@AndroidEntryPoint
class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {
    override val isBottomNavVisible = false

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBinding {
            register.setOnClickListener {
                val email = email.text.toString()
                val password = password.text.toString()
                val repeatPass = repeatPassword.text.toString()

                if (password == repeatPass) {
                    viewModel.register(email, password)
                } else {
                    handleErrorState(getString(R.string.registration_fragment_passwords_no_equals))
                }
            }

            auth.setOnClickListener {
                navigateSafety(RegistrationFragmentDirections.actionRegistrationFragmentToAuthFragment())
            }
        }

        viewModel.apply {
            subscribe(registerState, ::handleRegistrationState)
            subscribe(error, ::handleErrorState)
            start()
        }
    }

    private fun handleRegistrationState(isSuccess: Boolean) {
        if (isSuccess) {
            navigateSafety(RegistrationFragmentDirections.actionRegistrationFragmentToNotesFragment())
        }
    }

    private fun handleErrorState(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}