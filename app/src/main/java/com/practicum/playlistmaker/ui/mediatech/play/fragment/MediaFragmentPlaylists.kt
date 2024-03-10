package com.practicum.playlistmaker.ui.mediatech.play.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.ui.mediatech.fragment.MediatechFragmentDirections
import com.practicum.playlistmaker.ui.mediatech.play.PlayListViewHolder
import com.practicum.playlistmaker.ui.mediatech.play.PlayListsAdapter
import com.practicum.playlistmaker.ui.mediatech.play.view_model.MediaPlaylistsViewModel
import com.practicum.playlistmaker.ui.playlist.PlayListsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentPlaylists : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val mediaFragmentPlayLists by viewModel<MediaPlaylistsViewModel>()
    private val playListsAdapter = object : PlayListsAdapter(
        clickListener = {
            clickOnPlayList(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
            return PlayListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPlayLists.adapter = playListsAdapter

        binding.buttonNewPlaylist.setOnClickListener {
            val playList: PlayList? = null
            findNavController().navigate(
                MediatechFragmentDirections.actionMediaFragmentToAddPlayListFragment(playList)
            )
        }

        mediaFragmentPlayLists.observePlayListsState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlayListsState) {
        binding.rvPlayLists.isVisible = state is PlayListsState.PlayLists
        binding.tvError.isVisible = state is PlayListsState.Empty
        binding.ivPlaceholder.isVisible = state is PlayListsState.Empty
        if (state is PlayListsState.PlayLists) {
            playListsAdapter.playLists = state.playLists
        }
    }

    override fun onResume() {
        super.onResume()
        mediaFragmentPlayLists.requestPlayLists()
    }

    private fun clickOnPlayList(playList: PlayList) {
        findNavController().navigate(
            MediatechFragmentDirections.actionMediaFragmentPlaylistsToPlayListFragment(
                playList
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MediaFragmentPlaylists()
    }

}