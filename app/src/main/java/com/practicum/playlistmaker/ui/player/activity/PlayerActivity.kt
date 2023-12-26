package com.practicum.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val playerViewModel by viewModel<PlayerViewModel>()

    private var track: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra<Track>("track")
        }

        if (track == null) {
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        playerViewModel.playDuration.observe(this) { duration ->
            binding.duration.text = duration
        }

        binding.trackName.text = track!!.trackName
        binding.trackGroup.text = track!!.artistName
        binding.duration.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(Consts.TRACK_DURATION_INTRO_TIME * Consts.TRACK_DURATION_DELAY_TIME)
        binding.timeline.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track!!.trackTimeMillis)
        binding.album.text =
            if (track!!.collectionName!!.isNotEmpty()) track!!.collectionName else ""
        binding.year.text = track!!.releaseDate?.substring(0, 4)
        binding.genre.text = track!!.primaryGenreName
        binding.country.text = track!!.country

        Glide.with(this)
            .load(track!!.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_3x)
            .centerCrop()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dm2)))
            .into(binding.cover)

        binding.play.setOnClickListener {
            playerViewModel.playbackControl(track!!.previewUrl!!)
        }

        playerViewModel.playState.observe(this) { playState ->
            if (playState) {
                binding.play.setImageResource(R.drawable.pause_light)
            } else {
                binding.play.setImageResource(R.drawable.play_for_light)
            }
        }
        playerViewModel.playDuration.observe(this) { duration ->
            binding.duration.text = duration
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

}
