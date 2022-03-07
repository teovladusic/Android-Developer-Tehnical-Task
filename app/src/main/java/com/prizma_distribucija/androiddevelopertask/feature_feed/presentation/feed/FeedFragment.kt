package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentFeedBinding
import com.prizma_distribucija.androiddevelopertask.databinding.UserNotLoggedInDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private var _userNotLoggedInBinding: UserNotLoggedInDialogBinding? = null
    private val userNotLoggedInBinding get() = _userNotLoggedInBinding!!

    private val viewModel: FeedViewModel by viewModels()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var userNotLoggedInDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFeedBinding.bind(view)
        _userNotLoggedInBinding = UserNotLoggedInDialogBinding.inflate(layoutInflater)

        createUserNotLoggedInDialog()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuthenticated.collectLatest { isAuthenticated ->

                    if (isAuthenticated) {
                        userNotLoggedInDialog.dismiss()
                    } else {
                        onUserNotAuthenticated()
                    }
                }
            }
        }

        userNotLoggedInBinding.tvLogIn.setOnClickListener {
            onLoginClicked()
        }
    }

    private fun createUserNotLoggedInDialog() {
        userNotLoggedInDialog = AlertDialog.Builder(requireContext())
            .setView(userNotLoggedInBinding.root)
            .setCancelable(false)
            .create()
    }

    private fun onUserNotAuthenticated() {
        //stop video
        userNotLoggedInDialog.show()
    }

    private fun onLoginClicked() {
        userNotLoggedInDialog.dismiss()
        findNavController().navigate(R.id.action_global_loginFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _userNotLoggedInBinding = null
        _binding = null
    }
}