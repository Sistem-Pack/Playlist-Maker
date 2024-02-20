package com.practicum.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.MediaFragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.ui.playlist.PlayListsAdapter
import com.practicum.playlistmaker.ui.playlist.PlayListsState
import com.practicum.playlistmaker.ui.playlist.view_model.MediaPlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentPlaylists : Fragment() {
    private var _binding: MediaFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val mv by viewModel<MediaPlaylistsViewModel>()
    private val playListsAdapter by lazy { PlayListsAdapter { clickOnPlayList(it) } }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPlayLists.adapter = playListsAdapter

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_addPlayListFragment
            )
        }

        mv.observeLiveData().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        mv.observePlayListsState().observe(viewLifecycleOwner) {
            render(it)
        }
    }


    private fun render(state: PlayListsState) {
        when (state) {
            is PlayListsState.Empty -> {
                binding.rvPlayLists.visibility = View.GONE
                binding.ivPlaceholder.visibility = View.VISIBLE
                binding.tvError.visibility = View.VISIBLE
            }
            is PlayListsState.PlayLists -> {
                playListsAdapter.playLists = state.tracks
                binding.ivPlaceholder.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                binding.rvPlayLists.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mv.requestPl()
        //mv.requestPlayLists()
    }

    private fun clickOnPlayList(playList: PlayList) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MediaFragmentPlaylists {
            return MediaFragmentPlaylists()
        }
    }

}