package com.practicum.playlistmaker.ui.mediatech.play.add_to_playlist.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.Consts
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAddplaylistBinding
import com.practicum.playlistmaker.domain.search.models.PlayList
import com.practicum.playlistmaker.ui.mediatech.play.add_to_playlist.view_model.AddPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddplaylistBinding? = null
    private val binding get() = _binding!!
    private val addPlayListViewModel by viewModel<AddPlayListViewModel>()
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var pickImageUri: Uri? = null
    private var playList: PlayList? = null
    private val args: AddPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddplaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playList?.let {
            binding.toolbar.title = getString(R.string.edit)
            binding.playlistCreateButton.text = getString(R.string.save)
            binding.playListNameEditText.setText(it.name)
            binding.playListDescriptionEditText.setText(it.description)
            it.image?.let { imageName ->
                binding.addImage.setImageURI(
                    File(
                        File(
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            Consts.PLAY_LISTS_IMAGES_DIRECTORY
                        ), imageName
                    ).toUri()
                )
            }
            binding.playlistCreateButton.isEnabled = true
        }


        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.finish_creating_playlist))
            setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
        }

        binding.playListNameEditText.doOnTextChanged { s: CharSequence?, _, _, _ ->
            val descriptionNotEmpty = !binding.playListDescriptionEditText.text.isNullOrEmpty()
            binding.playlistCreateButton.isEnabled = !s.isNullOrEmpty() || descriptionNotEmpty
        }

        binding.toolbar.setOnClickListener {
            if (checkUnsavedData()) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.addImage.setImageURI(uri)
                    pickImageUri = uri
                }
            }
        binding.addImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.playlistCreateButton.setOnClickListener {
            if (addPlayListViewModel.clickDebounce()) {
                val name = binding.playListNameEditText.text.toString()
                val description = binding.playListDescriptionEditText.text.toString()
                if (name.isNotEmpty()) {
                    if (playList != null) {
                        addPlayListViewModel.editPlayList(
                            playList!!.playListId,
                            name = name,
                            description = description,
                            pickImageUri = pickImageUri
                        ) {
                            findNavController().popBackStack()
                        }
                    } else {
                        addPlayListViewModel.createPlayList(
                            name = name,
                            description = description,
                            pickImageUri = pickImageUri
                        ) {
                            Toast.makeText(
                                requireContext(),
                                String.format(
                                    resources.getText(R.string.playlist_created).toString(), name
                                ),
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }

                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playList = args.playList
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkUnsavedData()) {
                    confirmDialog.show()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun checkUnsavedData(): Boolean {
        playList?.let {
            return false
        }
        return (
                pickImageUri != null
                        || binding.playListNameEditText.text.toString().isNotEmpty()
                        || binding.playListDescriptionEditText.text.toString().isNotEmpty()
                )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}