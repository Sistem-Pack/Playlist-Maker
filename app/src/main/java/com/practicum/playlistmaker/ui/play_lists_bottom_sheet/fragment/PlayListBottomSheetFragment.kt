package com.practicum.playlistmaker.ui.play_lists_bottom_sheet.fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.databinding.FragmentPlaylistBottomSheetBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.ui.play_list_bottom_sheet.fragment.PlayListBottomSheetFragment
import com.practicum.playlistmaker.ui.play_lists_bottom_sheet.view_model.PlayListBottomSheetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlayListBottomSheetFragment(
    private val externalNavigator: ExternalNavigator,
    private val playList: PlayList,
    private val shareText: String
) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentPlaylistBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModelPlayListBottomSheet by viewModel<PlayListBottomSheetViewModel>()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelPlayListBottomSheet.tracksCount.observe(viewLifecycleOwner) { count ->
            playList.tracksCount = count
        }
        viewModelPlayListBottomSheet.observeTrackCount(playList.playListId)

        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Consts.PLAY_LISTS_IMAGES_DIRECTORY
        )
        Glide
            .with(binding.itemPlaylist.ivCoverPlaylist)
            .load(playList.image?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.track_pic_312)
            .into(binding.itemPlaylist.ivCoverPlaylist)

        binding.itemPlaylist.tvPlaylistName.text = playList.name

        binding.itemPlaylist.tvPlaylistTracksCount.text =
            binding.itemPlaylist.tvPlaylistTracksCount.resources.getQuantityString(
                R.plurals.plural_count_tracks, playList.tracksCount, playList.tracksCount
            )

        binding.buttonSharePlaylist.setOnClickListener {
            if (viewModelPlayListBottomSheet.clickDebounce()) {

                if (playList.tracksCount > 0) {
                    externalNavigator.sharePlayList(shareText)
                } else {
                    dismiss()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.playlist_is_empty_for_share),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.buttonEditPlaylist.setOnClickListener {
            if (viewModelPlayListBottomSheet.clickDebounce()) {
                dismiss()
                findNavController().navigate(
                    PlayListBottomSheetFragmentDirections.actionPlayListBottomSheetFragmentToAddPlayListFragment(
                        playList
                    )
                )
            }
        }

        binding.buttonDeletePlaylist.setOnClickListener {
            if (viewModelPlayListBottomSheet.clickDebounce()) {
                confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle(resources.getText(R.string.playlist_delete))
                    setMessage(getString(R.string.playlist_delete_question, playList.name))
                    setNegativeButton(resources.getText(R.string.no)) { _, _ ->
                    }
                    setPositiveButton(resources.getText(R.string.yes)) { _, _ ->

                        viewModelPlayListBottomSheet.deletePlaylist(
                            playList
                        ) {
                            Toast.makeText(
                                requireContext(),
                                resources.getText(R.string.playlist_deleted),
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().popBackStack()
                        }
                    }
                }
                confirmDialog.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PlaylistBottomSheet"

        fun newInstance(
            externalNavigator: ExternalNavigator,
            playList: PlayList,
            shareText: String
        ): PlayListBottomSheetFragment {
            return PlayListBottomSheetFragment(externalNavigator, playList, shareText)
        }
    }
}