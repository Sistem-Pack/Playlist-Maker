package com.practicum.playlistmaker.ui.mediatech.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.MediaFragmentFavoriteTracksBinding
import com.practicum.playlistmaker.ui.mediatech.activity.view_model.MediaFavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragmentFavoriteTracks : Fragment() {

    private var _binding: MediaFragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val mfftViewModel by viewModel<MediaFavoriteTracksViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MediaFragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MediaFragmentFavoriteTracks()
    }
}