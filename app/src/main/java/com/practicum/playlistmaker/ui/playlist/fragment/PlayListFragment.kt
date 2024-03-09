package com.practicum.playlistmaker.ui.playlist.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.player.PlayListState
import com.practicum.playlistmaker.ui.playlist.play_lists_bottom_sheet.fragment.PlayListBottomSheetFragment
import com.practicum.playlistmaker.ui.playlist.view_model.PlayListViewModel
import com.practicum.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlayListFragment : Fragment() {
    private val playListViewModel: PlayListViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playList: PlayList
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val args: PlayListFragmentArgs by navArgs()
    private val playListTracksAdapter by lazy { TrackAdapter(::clickOnTrack, ::longClickOnTrack) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tracksRV.adapter = playListTracksAdapter
        playListViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListState.PlayListTracks -> {
                    showTracks(it.tracks)
                }

                is PlayListState.PlayListInfo -> {
                    playList = it.playList
                    showPlayList()
                }
            }
        }
        initOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        playListViewModel.requestPlayListInfo(playList.playListId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playList = args.playList ?: return
    }

    private fun initOnClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.iconShare.setOnClickListener {
            if (playListViewModel.clickDebounce()) {
                if (playListTracksAdapter.listTrack.isNotEmpty()) {
                    playListViewModel.shareText(buildShareText())
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.playlist_is_empty_for_share),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.iconMore.setOnClickListener {
            if (playListViewModel.clickDebounce()) {
                PlayListBottomSheetFragment.newInstance(
                    playList,
                    buildShareText()
                ).show(childFragmentManager, PlayListBottomSheetFragment.TAG)
            }
        }
    }

    private fun buildShareText(): String {
        var shareText =
            "${playList.name}\n${playList.description}\n" + binding.playListInfoCountTracks.resources.getQuantityString(
                R.plurals.plural_count_tracks,
                playListTracksAdapter.listTrack.size,
                playListTracksAdapter.listTrack.size
            ) + "\n"
        playListTracksAdapter.listTrack.forEachIndexed { index, track ->
            shareText += "\n ${index + 1}. ${track.artistName} - ${track.trackName} (" + SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis) + ")"
        }
        return shareText
    }

    private fun showPlayList() {
        binding.apply {
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Consts.PLAY_LISTS_IMAGES_DIRECTORY
            )
            Glide
                .with(ivCoverPlaylist)
                .load(playList.image?.let { imageName -> File(filePath, imageName) })
                .placeholder(R.drawable.track_pic_312)
                .into(ivCoverPlaylist)

            tvPlaylistName.text = playList.name
            tvPlaylistName.isSelected = true

            if (playList.description.isNotEmpty()) {
                playListDescription.text = playList.description
                playListDescription.isSelected = true
                playListDescription.visibility = View.VISIBLE
            } else {
                playListDescription.visibility = View.GONE
            }

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
            binding.bottomBlankView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        bottomSheetBehavior.peekHeight = binding.bottomBlankView.height
                        binding.bottomBlankView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_is_empty),
                Toast.LENGTH_SHORT
            ).show()
        }
        playListTracksAdapter.setTracks(tracks)
        playListTracksAdapter.notifyDataSetChanged()
        var durationSum = 0L
        playListTracksAdapter.listTrack.forEach { track ->
            durationSum += track.trackTimeMillis ?: 0
        }
        durationSum = TimeUnit.MILLISECONDS.toMinutes(durationSum)
        binding.playListInfoDuration.text =
            binding.playListInfoDuration.resources.getQuantityString(
                R.plurals.plural_minutes,
                durationSum.toInt(),
                durationSum
            )
        binding.playListInfoCountTracks.text =
            binding.playListInfoCountTracks.resources.getQuantityString(
                R.plurals.plural_count_tracks,
                playListTracksAdapter.listTrack.size,
                playListTracksAdapter.listTrack.size
            )
    }

    private fun clickOnTrack(track: Track) {
        if (playListViewModel.clickDebounce()) {
            findNavController().navigate(
                PlayListFragmentDirections.actionPlayListFragmentToPlayerFragment(
                    track
                )
            )
        }
    }

    private fun longClickOnTrack(track: Track) {
        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(resources.getText(R.string.delete_track))
            setMessage(resources.getText(R.string.delete_track_question))
            setNegativeButton(resources.getText(R.string.cancel)) { _, _ ->
            }
            setPositiveButton(resources.getText(R.string.delete)) { _, _ ->
                playListViewModel.deleteTrackFromPlaylist(track.trackId, playList.playListId)
            }
        }
        confirmDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}