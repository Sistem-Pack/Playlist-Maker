package com.practicum.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.search.models.Track
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchState
import com.practicum.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModel<SearchViewModel>()

    private var adapterTracks: TrackAdapter? = null
    private var adapterTracksHistory: TrackAdapter? = null

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var searchText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapterTracks = TrackAdapter {
            intentAudioPlayer(it)
        }
        adapterTracksHistory = TrackAdapter {
            intentAudioPlayer(it, true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trackRecyclerView.adapter = adapterTracks
        binding.searchHistory.adapter = adapterTracksHistory
        searchViewModel.showHistoryTracks()

        binding.clearText.setOnClickListener {
            binding.editViewSearch.setText("")
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
                binding.editViewSearch.windowToken,
                0
            )
            searchViewModel.showHistoryTracks()
        }

        binding.searchRefreshButton.setOnClickListener {
            searchViewModel.searchDebounce(binding.editViewSearch.text.toString())
        }

        binding.cleanHistoryButton.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        binding.editViewSearch.setOnFocusChangeListener { _, hasFocus ->
            searchViewModel.searchFocusChanged(hasFocus, binding.editViewSearch.text.toString())
        }

        binding.editViewSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editViewSearch.hasFocus() && searchText.isEmpty()) {
                    searchViewModel.showHistoryTracks()
                } else if (searchText != binding.editViewSearch.text.toString()) {
                    searchViewModel.searchDebounce(searchText)
                }
            }
            false
        }

        searchViewModel.searchScreenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        // отправляем все на поиск
        binding.editViewSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
                searchText = s?.toString() ?: ""
                if (binding.editViewSearch.hasFocus() && searchText.isEmpty()) {
                    searchViewModel.showHistoryTracks()
                } else searchViewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = binding.editViewSearch.text.toString()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapterTracks = null
        adapterTracksHistory = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Consts.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun intentAudioPlayer(track: Track, updateHistoryLayout: Boolean = false) {
        if (clickDebounce()) {
            searchViewModel.addTrackToSearchHistory(track = track)
            if (updateHistoryLayout) {
                searchViewModel.showHistoryTracks()
            }
            //this.findNavController().navigate(R.id.activityPlayer)
            //findNavController().navigate(R.id.playerActivityEx)
            val navController = Navigation.findNavController(requireActivity(), R.id.Player)
            navController.navigate(R.id.Player)
            /*Intent(requireContext(), PlayerActivity::class.java).apply {
                this.putExtra(resources.getString(R.string.track), track)
                startActivity(this)
            }*/
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: SearchState) {
        if ((binding.editViewSearch.text.toString() == "") && state is SearchState.Content)
            searchViewModel.showHistoryTracks()
        if (state is SearchState.Content && binding.trackRecyclerView.adapter != null) {
            adapterTracks!!.setTracks(state.tracks)
            binding.trackRecyclerView.adapter!!.notifyDataSetChanged()
        } else if (state is SearchState.SearchHistory && binding.searchHistory.adapter != null) {
            adapterTracksHistory!!.setTracks(state.tracks)
            binding.searchHistory.adapter!!.notifyDataSetChanged()
        }
        binding.trackRecyclerView.visibility =
            if (state is SearchState.Content) View.VISIBLE else View.GONE
        binding.noConnectionErrorLayout.visibility =
            if (state is SearchState.Error) View.VISIBLE else View.GONE
        binding.notFoundLayout.visibility =
            if (state is SearchState.Empty) View.VISIBLE else View.GONE
        binding.historyLayout.visibility =
            if (state is SearchState.SearchHistory) View.VISIBLE else View.GONE
        binding.progressBar.visibility =
            if (state is SearchState.Loading) View.VISIBLE else View.GONE
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(Consts.SEARCH_TEXT, "")
        }
        if (searchText.isNotEmpty()) {
            binding.editViewSearch.setText(searchText)
            searchViewModel.searchDebounce(searchText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Consts.SEARCH_TEXT, searchText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

}