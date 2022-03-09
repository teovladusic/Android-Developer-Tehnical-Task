package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.databinding.AthleteItemBinding
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AthletesAdapter(
    private val athletes: List<Athlete>
) : RecyclerView.Adapter<AthletesAdapter.AthletesViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthletesViewHolder {
        context = parent.context
        val binding = AthleteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AthletesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AthletesViewHolder, position: Int) {
        val athlete = athletes[position]
        holder.bind(athlete)
    }

    override fun getItemCount() = athletes.size

    inner class AthletesViewHolder(
        private val binding: AthleteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.default_user)
            .error(R.drawable.default_user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        fun bind(athlete: Athlete) {
            binding.tvAthleteName.text = athlete.name
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(context).load(athlete.avatar).apply(options)
                    .into(binding.imgAthleteAvatar)
            }
        }
    }
}