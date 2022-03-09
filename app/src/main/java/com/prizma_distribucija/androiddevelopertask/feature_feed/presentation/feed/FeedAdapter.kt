package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.databinding.AthleteItemBinding
import com.prizma_distribucija.androiddevelopertask.databinding.FeedItemBinding
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedAdapter(
    private val listener: OnItemClickListener,
    private val feed: List<Feed>
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        context = parent.context
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feedItem = feed[position]
        holder.bind(feedItem)
    }

    override fun getItemCount() = feed.size

    inner class FeedViewHolder(
        val binding: FeedItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.videoView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onVideoClick()
                }
            }
        }

        fun bind(feed: Feed) {
            binding.tvAuthorName.text = feed.author.name
            binding.tvVideoDescription.text = feed.description
            binding.videoView.setVideoPath(feed.video.url)
            binding.videoView.setOnPreparedListener {
                it.isLooping = true
            }
            binding.videoView.start()
        }

        fun onPlayVideo() {
            CoroutineScope(Dispatchers.Main).launch {
                binding.imgViewPlayStopVideo.setImageResource(R.drawable.ic_play_video)
                binding.imgViewPlayStopVideo.visibility = View.VISIBLE
                delay(3000)
                binding.imgViewPlayStopVideo.visibility = View.GONE
            }
        }

        fun onPauseVideo() {
            CoroutineScope(Dispatchers.Main).launch {
                binding.imgViewPlayStopVideo.setImageResource(R.drawable.ic_pause_video)
                binding.imgViewPlayStopVideo.visibility = View.VISIBLE
                delay(3000)
                binding.imgViewPlayStopVideo.visibility = View.GONE
            }
        }
    }

    interface OnItemClickListener {
        fun onVideoClick()
    }
}