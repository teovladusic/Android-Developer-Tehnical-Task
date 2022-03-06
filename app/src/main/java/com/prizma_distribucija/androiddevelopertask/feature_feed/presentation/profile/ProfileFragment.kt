package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.profile

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentProfileBinding
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserStatus.collectLatest { status ->
                    when (status) {
                        is Resource.Loading -> {
                            handleGetUserLoading()
                        }

                        is Resource.Success -> {
                            handleGetUserSuccess(status.data!!)
                        }

                        is Resource.Error -> {
                            handleGetUserError(status.message ?: "Unknown error appeared.")
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collectLatest { user ->
                    if (user == null) {
                        return@collectLatest
                    }

                    populateUserUi(user)
                }
            }


        }
    }

    private fun populateUserUi(user: User) {
        binding.apply {
            tvName.text = user.name
            setImageWithGlide(url = user.avatar, imgView = imgViewUserPicture)
            tvViewsCount.text = user.views
            tvVideoPlaysCount.text = user.videoPlays
            tvPlanName.text = user.plan.type

            val adapter = AthletesAdapter(user.athletes)
            recViewAthletes.adapter = adapter
            recViewAthletes.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.default_user)
        .error(R.drawable.default_user)
        .diskCacheStrategy(DiskCacheStrategy.ALL)


    private fun setImageWithGlide(url: String, imgView: ImageView) =
        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(requireContext()).load(url).apply(options).into(imgView)
        }

    private fun handleGetUserLoading() {
        binding.apply {
            setNonLoadingLayoutVisibility(View.GONE)
            setProgressBarVisibility(View.VISIBLE)
        }
    }

    private fun handleGetUserSuccess(user: User) {
        binding.apply {
            setNonLoadingLayoutVisibility(View.VISIBLE)
            setProgressBarVisibility(View.GONE)
        }
        viewModel.onGetUserSuccess(user)
    }

    private fun handleGetUserError(message: String) {
        binding.apply {
            setNonLoadingLayoutVisibility(View.VISIBLE)
            setProgressBarVisibility(View.GONE)
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setNonLoadingLayoutVisibility(visibility: Int) {
        binding.apply {
            constraintLayout.visibility = visibility
            constraintLayoutStats.visibility = visibility
            recViewAthletes.visibility = visibility
            tvAthletes.visibility = visibility
        }
    }

    private fun setProgressBarVisibility(visibility: Int) {
        binding.progressBarGetProfile.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}