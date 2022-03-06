package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentSignUpBinding
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUpEvents.collectLatest { event ->
                    when (event) {
                        is SignUpViewModel.SignUpEvents.ShowMessage -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT)
                                .show()
                        }

                        is SignUpViewModel.SignUpEvents.Registered -> {
                            findNavController().navigate(R.id.action_global_feedFragment)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerUserStatus.collectLatest { status ->
                    when (status) {
                        is Resource.Loading -> {
                            handleRegisterLoading()
                        }

                        is Resource.Success -> {
                            handleRegisterSuccess(status.data!!)
                        }

                        is Resource.Error -> {
                            handleRegisterError(status.message ?: "Unknown error occurred")
                        }
                    }
                }
            }
        }


        binding.btnSignUp.setOnClickListener {
            onSignUpClicked()
        }
    }

    private fun handleRegisterLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun handleRegisterSuccess(data: String) {
        binding.progressBar.visibility = View.GONE
        viewModel.onRegisterSuccess(data)
    }

    private fun handleRegisterError(data: String) {
        binding.progressBar.visibility = View.GONE
        Snackbar.make(binding.root, data, Snackbar.LENGTH_SHORT).show()

    }

    private fun onSignUpClicked() {
        val email = binding.textInputSignUpEmail.text.toString()
        val password = binding.textInputSignUpPassword.text.toString()
        val fullName = binding.textInputSignUpFullName.text.toString()
        val signUpUserData = SignUpUserData(email, password, fullName)
        viewModel.onSignUpClicked(signUpUserData)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}