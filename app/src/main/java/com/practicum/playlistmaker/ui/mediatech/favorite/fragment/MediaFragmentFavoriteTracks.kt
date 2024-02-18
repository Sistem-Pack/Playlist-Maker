package com.practicum.playlistmaker.ui.mediatech.favorite.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.databinding.MediaFragmentFavoriteTracksBinding
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.mediatech.favorite.FavoriteTrackState
import com.practicum.playlistmaker.ui.mediatech.favorite.view_model.MediaFavoriteTracksViewModel
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentFavoriteTracks : Fragment() {

    private var _binding: MediaFragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MediaFavoriteTracksViewModel>()
    private val favoriteTrackAdapter by lazy { TrackAdapter { startAdapter(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MediaFragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.rvFavouriteTracks.adapter = favoriteTrackAdapter

        viewModel.waitData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.waitData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAdapter(track: Track) {
        if (viewModel.clickDebounce()) {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
                .apply { putExtra(Consts.TRACK, track) }
            viewModel.clickDebounce()
            startActivity(intent)
        }
    }

    private fun render(state: FavoriteTrackState) {
        when (state) {
            is FavoriteTrackState.Loading -> {
                with(binding) {
                    ivPlaceholder.isVisible = false
                    tvError.isVisible = false
                    rvFavouriteTracks.isVisible = false
                }
            }

            is FavoriteTrackState.Content -> {
                favoriteTrackAdapter.clearTracks()
                favoriteTrackAdapter.listTrack = state.tracks as ArrayList<Track>
                favoriteTrackAdapter.notifyDataSetChanged()
                with(binding) {
                    rvFavouriteTracks.isVisible = true
                    ivPlaceholder.isVisible = false
                    tvError.isVisible = false
                }
            }

            is FavoriteTrackState.Empty -> {
                with(binding) {
                    ivPlaceholder.isVisible = true
                    tvError.isVisible = true
                    rvFavouriteTracks.isVisible = false
                }
            }
        }
    }

    companion object {
        fun newInstance() = MediaFragmentFavoriteTracks()
    }
}