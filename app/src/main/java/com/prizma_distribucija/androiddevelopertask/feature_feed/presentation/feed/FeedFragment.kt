package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentFeedBinding
import com.prizma_distribucija.androiddevelopertask.databinding.UserNotLoggedInDialogBinding
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed), FeedAdapter.OnItemClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private var _userNotLoggedInBinding: UserNotLoggedInDialogBinding? = null
    private val userNotLoggedInBinding get() = _userNotLoggedInBinding!!

    private val viewModel: FeedViewModel by viewModels()

    lateinit var videoAdapter: FeedAdapter

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    lateinit var userNotLoggedInDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFeedBinding.bind(view)
        _userNotLoggedInBinding = UserNotLoggedInDialogBinding.inflate(layoutInflater)

        createUserNotLoggedInDialog()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getFeedStatus.collectLatest { status ->
                    when (status) {
                        is Resource.Error -> {
                            handleGetFeedError()
                        }
                        is Resource.Loading -> {
                            handleGetFeedLoading()
                        }
                        is Resource.Success -> {
                            handleGetFeedSuccess(status.data ?: emptyList())
                        }
                    }
                }
            }
        }

        val callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onPageChanged(position)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(callback)

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shouldVideoPlay.collect { shouldVideoPlay ->
                    try {
                        val holder =
                            (binding.viewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
                                viewModel.viewPagerPosition
                            ) as FeedAdapter.FeedViewHolder

                        if (shouldVideoPlay) {
                            holder.binding.videoView.start()
                            holder.onPlayVideo()
                        } else {
                            holder.binding.videoView.pause()
                            holder.onPauseVideo()
                        }
                    } catch (e: NullPointerException) {
                        return@collect
                    }
                }
            }
        }

        userNotLoggedInBinding.tvLogIn.setOnClickListener {
            onLoginClicked()
        }
    }

    override fun onVideoClick() {
        viewModel.onVideoClick()
    }

    private fun handleGetFeedLoading() {
        binding.progressBarGetFeed.visibility = View.VISIBLE
    }

    private fun handleGetFeedSuccess(data: List<Feed>) {
        binding.progressBarGetFeed.visibility = View.GONE
        videoAdapter = FeedAdapter(this, data)
        binding.viewPager.adapter = videoAdapter
    }

    private fun handleGetFeedError() {
        binding.progressBarGetFeed.visibility = View.GONE
        binding.tvGetFeedError.visibility = View.VISIBLE
    }

    private fun createUserNotLoggedInDialog() {
        userNotLoggedInDialog = AlertDialog.Builder(requireContext())
            .setView(userNotLoggedInBinding.root)
            .setCancelable(false)
            .create()
    }

    private fun onUserNotAuthenticated() {
        viewModel.onVideoClick()
        userNotLoggedInDialog.show()
    }

    private fun onLoginClicked() {
        userNotLoggedInDialog.dismiss()
        findNavController().navigate(R.id.action_global_loginFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        userNotLoggedInDialog.dismiss()
        _userNotLoggedInBinding = null
        _binding = null
    }
}