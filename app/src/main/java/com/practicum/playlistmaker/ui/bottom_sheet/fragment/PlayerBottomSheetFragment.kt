package com.practicum.playlistmaker.ui.bottom_sheet.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentBottomSheetBinding
import com.practicum.playlistmaker.domain.playlist.models.PlayListsState
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.bottom_sheet.view_model.PlayerBottomSheetViewModel
import com.practicum.playlistmaker.ui.playlist.PlayListViewHolder
import com.practicum.playlistmaker.ui.playlist.PlayListsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerBottomSheetFragment(val track: Track) : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModelPlayerBottomSheet by viewModel<PlayerBottomSheetViewModel>()
    private val playListsAdapter by lazy { PlayListsAdapter { addTrackToPlayList(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistsRecycler.adapter = playListsAdapter

        viewModelPlayerBottomSheet.observePlayListsState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsState.Empty -> binding.playlistsRecycler.visibility = View.GONE
                is PlayListsState.PlayLists -> {
                    playListsAdapter.playLists = it.playLists
                    binding.playlistsRecycler.visibility = View.VISIBLE
                }

                is PlayListsState.AddTrackToPlayListResult -> {
                    if (it.isAdded) {
                        showToast(getString(R.string.added_to_playlist, it.playListName))
                        dismiss()
                    } else {
                        showToast(getString(R.string.already_added_to_playlist, it.playListName))
                    }
                }
            }
        }

        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_addPlayListFragment
            )
        }

    }

    override fun onResume() {
        super.onResume()
        viewModelPlayerBottomSheet.requestPlayLists()
    }

    private fun addTrackToPlayList(playList: PlayList) {
        if (viewModelPlayerBottomSheet.clickDebounce()) {
            viewModelPlayerBottomSheet.addTrackToPlayList(track, playList)
        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(track: Track): PlayerBottomSheetFragment {
            return PlayerBottomSheetFragment(track)
        }
    }
}