package com.practicum.playlistmaker.ui.player.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModelFactory
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModelFactory
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel

    private var track: Track? = null

    /*//private lateinit var play: ImageButton
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String? = null
    private var mainThreadHandler: Handler? = null
    private lateinit var durationView: TextView
    private var currentTrackTime: Long = 0*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel =
            ViewModelProvider(this, PlayerViewModelFactory(track!!))[PlayerViewModel::class.java]

        val track = intent.serializable<Track>(Constants.TRACK)

        viewModel = ViewModelProvider(this, PlayerActivityViewModelFactory(track!!))
            .get(PlayerViewModel::class.java)

        binding.ivBack.setOnClickListener {
            finish()
        }

        viewModel.playCountdown.observe(this) { duration ->
            binding.tvCountdown.text = duration
        }

        Glide.with(this)
            .load(getCoverArtwork(track!!.artworkUrl100))
            .placeholder(R.drawable.plug)
            .centerInside()
            .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.frame_8dp)))
            .into(binding.ivPictureArtist)

        binding.tvNameTrack.text = track.trackName
        binding.tvNameArtist.text = track.artistName

        binding.tvDurationName.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.tvAlbumName.text =
            if (track.collectionName.isNullOrEmpty()) ({
                binding.tvAlbumName.visibility = View.GONE
                binding.tvAlbum.visibility = View.GONE
            }).toString() else track.collectionName

        if (track.releaseDate.isNullOrEmpty()) {
            binding.tvYearName.text = ""
        } else binding.tvYearName.text = track.releaseDate.substring(0, 4)

        binding.tvGenreName.text = track.primaryGenreName
        binding.tvCountryName.text = track.country

        binding.ivPlayTrack.setOnClickListener {
            viewModel.playbackControl(track.previewUrl)
        }

        viewModel.playState.observe(this) { playState ->
            if (playState) {
                binding.ivPlayTrack.setImageResource(R.drawable.pause_button)
            } else {
                binding.ivPlayTrack.setImageResource(R.drawable.play_button)
            }
        }
        viewModel.playCountdown.observe(this) { duration ->
            binding.tvCountdown.text = duration
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

        /*play = findViewById(R.id.play)
        val coverImage = findViewById<ImageView>(R.id.cover)
        val backButton = findViewById<View>(R.id.back_button)
        val trackName = findViewById<TextView>(R.id.track_name)
        val trackGroup = findViewById<TextView>(R.id.track_group)
        durationView = findViewById<TextView>(R.id.duration)
        val timeline = findViewById<TextView>(R.id.timeline)
        val albumName = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.play.isEnabled = false
        binding.play.alpha = 0.5f

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra<Track>("track")
        }

        if (track == null) {
            finish()
        } else {
            url = track!!.previewUrl
            preparePlayer()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.play.setOnClickListener {
            val currentTimeTrackPosition =
                if (currentTrackTime != 0L) currentTrackTime else INTRO_TIME
            startTimer(currentTimeTrackPosition)
            playbackControl()
        }

        mediaPlayer.setOnCompletionListener {
            /*if ((applicationContext as App).darkTheme) {
                play.setImageResource(R.drawable.play_dark)
            } else play.setImageResource(R.drawable.play_for_light)*/
            playerState = STATE_PREPARED
            currentTrackTime = 0L
        }

        fun getTrackInfo(track: Track) {

            trackName.text = track.trackName
            trackGroup.text = track.artistName
            durationView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(INTRO_TIME * DELAY)
            timeline.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            albumName.text =
                if (track.collectionName!!.isNotEmpty()) track.collectionName else ""
            year.text = track.releaseDate?.substring(0, 4)
            genre.text = track.primaryGenreName
            country.text = track.country

            val artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

            Glide.with(this)
                .load(artworkUrl512)
                .placeholder(R.drawable.album_3x)
                .centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.dm2)))
                .into(coverImage)
        }

        getTrackInfo(track!!)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask(0, 0))
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            play.alpha = 1f
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        /*if ((applicationContext as App).darkTheme) {
                play.setImageResource(R.drawable.pause_for_black)
        } else play.setImageResource(R.drawable.pause_light)*/
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        /*if ((applicationContext as App).darkTheme) {
            play.setImageResource(R.drawable.play_dark)
        } else play.setImageResource(R.drawable.play_for_light)*/
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask(0, 0))
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        mainThreadHandler?.post(
            createUpdateTimerTask(startTime, duration * DELAY)
        )
    }

    private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val remainingTime = duration - elapsedTime

                    if (remainingTime > 0) {
                        val seconds = remainingTime / DELAY
                        durationView.text =
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(seconds * DELAY)
                        currentTrackTime = seconds
                        mainThreadHandler?.postDelayed(this, DELAY)
                    } else {
                        durationView.text = "00:00"
                    }
                }
            }
        }*/
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
        private const val INTRO_TIME = 30L
    }

}