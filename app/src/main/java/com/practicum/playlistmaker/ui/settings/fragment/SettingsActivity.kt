package com.practicum.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.themeSettingsState.observe(viewLifecycleOwner) { themeSettings ->
            binding.modeSwitch.isChecked = themeSettings.darkTheme
        }

        binding.modeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }

        binding.offer.setOnClickListener {
            settingsViewModel.openOffer()
        }

        binding.send.setOnClickListener {
            settingsViewModel.sendMail()
        }

        binding.share.setOnClickListener {
            settingsViewModel.shareApp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}