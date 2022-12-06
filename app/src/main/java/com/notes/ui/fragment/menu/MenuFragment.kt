package com.notes.ui.fragment.menu

import android.os.Bundle
import android.view.View
import com.notes.databinding.FragmentMenuBinding
import com.notes.ui.base.BaseFragment

/**
 * @author Fedotov Yakov
 */
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBinding {
            aboutApp.setOnClickListener {
                navigateSafety(MenuFragmentDirections.actionMenuFragmentToAboutAppFragment())
            }
        }
    }
}