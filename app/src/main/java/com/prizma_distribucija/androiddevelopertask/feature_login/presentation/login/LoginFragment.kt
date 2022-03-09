package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentLoginBinding
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.tvRegister.setOnClickListener {
            onRegisterClicked()
        }

        binding.btnLogin.setOnClickListener {
            onLoginClicked()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginEvents.collectLatest { event ->
                    when (event) {
                        is LoginViewModel.LoginEvents.ShowMessage -> {
                            Snackbar.make(
                                binding.root,
                                "Please fill out all information.",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }

                        is LoginViewModel.LoginEvents.LoggedIn -> {
                            findNavController().navigate(R.id.action_global_feedFragment)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStatus.collect { status ->
                    when (status) {
                        is Resource.Loading -> {
                            handleLoginLoading()
                        }

                        is Resource.Success -> {
                            handleLoginSuccess(status.data ?: "")
                        }

                        is Resource.Error -> {
                            handleLoginError(status.message ?: "Unknown error appeared")
                        }
                    }
                }
            }
        }
    }

    private fun handleLoginLoading() {
        binding.progressBarLogin.visibility = View.VISIBLE
    }

    private fun handleLoginSuccess(data: String) {
        binding.progressBarLogin.visibility = View.GONE
        viewModel.onLoginSuccess(data)
    }

    private fun handleLoginError(data: String) {
        binding.progressBarLogin.visibility = View.GONE
        Snackbar.make(binding.root, data, Snackbar.LENGTH_SHORT).show()
    }

    private fun onLoginClicked() {
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()
        val loginUserData = LoginUserData(email, password)

        viewModel.onLoginClicked(loginUserData)
    }

    private fun onRegisterClicked() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}