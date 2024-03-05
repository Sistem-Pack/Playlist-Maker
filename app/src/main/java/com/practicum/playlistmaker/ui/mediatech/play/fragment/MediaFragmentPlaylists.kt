package com.practicum.playlistmaker.ui.mediatech.play.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.ui.mediatech.fragment.MediatechFragmentDirections
import com.practicum.playlistmaker.ui.mediatech.play.PlayListViewHolder
import com.practicum.playlistmaker.ui.mediatech.play.PlayListsAdapter
import com.practicum.playlistmaker.ui.playlist.PlayListsState
import com.practicum.playlistmaker.ui.mediatech.play.view_model.MediaPlaylistsViewModel
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
        when (state) {
            is PlayListsState.Empty -> {
                binding.rvPlayLists.visibility = View.GONE
                binding.ivPlaceholder.visibility = View.VISIBLE
                binding.tvError.visibility = View.VISIBLE
            }

            is PlayListsState.PlayLists -> {
                playListsAdapter.playLists = state.playLists
                binding.ivPlaceholder.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                binding.rvPlayLists.visibility = View.VISIBLE
            }
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