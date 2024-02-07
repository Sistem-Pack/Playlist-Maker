package com.practicum.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.player.models.PlayerState
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
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
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Track>("track")
        } as Track

        if (track == null) {
            finish()
        }

        playerViewModel.checkIsFavorite(track!!.trackId)

        playerViewModel.favoriteState.observe(this) { isFavorite ->
            setLikeIcon(isFavorite)
        }


        binding.backButton.setOnClickListener {
            finish()
        }

        playerViewModel.playDuration.observe(this) { duration ->
            binding.duration.text = duration
        }

        playerViewModel.playState.observe(this) { playState ->
            when (playState) {
                PlayerState.PLAYING -> binding.play.setImageResource(R.drawable.pause_light)
                PlayerState.PREPARED, PlayerState.DEFAULT, PlayerState.PAUSED ->
                    binding.play.setImageResource(R.drawable.play_for_light)
            }
        }

        track!!.previewUrl?.let { playerViewModel.prepare(it) }

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
            playerViewModel.changePlayerState()
        }

        binding?.like?.setOnClickListener { playerViewModel.onFavoriteClicked(track = track!!) }

    }

    private fun setLikeIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding?.like?.setImageResource(R.drawable.like_up)
        } else {
            binding?.like?.setImageResource(R.drawable.like_down)
        }
    }

    override fun onPause() {
        playerViewModel.pausePlayer()
        super.onPause()
    }

    override fun onStart() {
        playerViewModel.startPlayer()
        super.onStart()
    }

    override fun onResume() {
        playerViewModel.resumePlayer()
        super.onResume()
    }

}
