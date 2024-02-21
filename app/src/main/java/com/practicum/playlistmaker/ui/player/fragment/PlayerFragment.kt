package com.practicum.playlistmaker.ui.player.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.player.models.PlayerState
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.bottom_sheet.fragment.PlayerBottomSheetFragment
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel by viewModel<PlayerViewModel>()

    private var track: Track? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.playDuration.observe(viewLifecycleOwner) { duration ->
            binding.duration.text = duration
        }

        playerViewModel.playState.observe(viewLifecycleOwner) { playState ->
            when (playState) {
                PlayerState.PLAYING -> binding.play.setImageResource(R.drawable.pause_light)
                PlayerState.PREPARED, PlayerState.DEFAULT, PlayerState.PAUSED ->
                    binding.play.setImageResource(R.drawable.play_for_light)
            }
        }
        playerViewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            setLikeIcon(isFavorite)
        }

        track?.let { playerViewModel.checkIsFavorite(it.trackId) }
        binding.play.setOnClickListener {
            playerViewModel.changePlayerState()
        }

        binding.like.setOnClickListener { playerViewModel.onFavoriteClicked(track = track!!) }

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

        binding.addToTrack.setOnClickListener {
            PlayerBottomSheetFragment.newInstance(track!!).show(childFragmentManager, Consts.TAG)
        }

    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("track", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("track")
        } as Track

        if (track == null) {
            findNavController().popBackStack();
        }
    }

    private fun setLikeIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.like.setImageResource(R.drawable.like_down)
        } else {
            binding.like.setImageResource(R.drawable.like_up)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
