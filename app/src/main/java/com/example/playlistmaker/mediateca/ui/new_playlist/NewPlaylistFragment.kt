package com.example.playlistmaker.mediateca.ui.new_playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.extension.onTextChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class NewPlaylistFragment : Fragment(R.layout.fragment_new_playlist) {

    private val binding by viewBinding(FragmentNewPlaylistBinding::bind)
    private val playlistId: Int by lazy { arguments?.getInt("playlistId") ?: -1 }
    private val viewModel: NewPlaylistViewModel by viewModel {
        parametersOf(playlistId)
    }

    private val picker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            saveImageToPrivateStorage(uri)
        }
    }

    private val dialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.close_play_list_title))
            .setMessage(getString(R.string.close_play_list_dialog_message))
            .setPositiveButton(getString(R.string.positive_button)) {_, _ -> finish() }
            .setNegativeButton(getString(R.string.negative_button)) { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private fun onBackPressed() {

        if (playlistId > -1) {
            finish()
            return
        }

        val state = viewModel.observeUi().value
        if (state.createEnabled || state.image != null || state.description?.isNotEmpty() == true) {
            dialog.show()
        } else {
            finish()
        }
    }

    private fun finish() {
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playListNameEditText.onTextChanged { text -> viewModel.onNameChanged(text) }
        binding.playListDescriptionEditText.onTextChanged { text -> viewModel.setDescription(text) }

        if (playlistId > -1) {
            binding.appTitle.text = getString(R.string.edit_title)
            binding.createBut.text = getString(R.string.save)
        } else {
            binding.appTitle.text = getString(R.string.new_playlist)
            binding.createBut.text = getString(R.string.create)
        }

        observeUi()
        binding.playListNameEditText.onTextChanged { text -> viewModel.onNameChanged(text) }

        binding.newPlayListImage.setOnClickListener {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.createBut.setOnClickListener {
            val name = binding.playListNameEditText.text?.toString().orEmpty()
            val description = binding.playListDescriptionEditText.text?.toString().orEmpty()
            viewModel.create(name, description)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
    }

    private fun observeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeUi().collect { state ->
                    binding.createBut.isEnabled = state.createEnabled
                    updateImage(state.image)

                    if (state.name != binding.playListNameEditText.text?.toString()) {
                        binding.playListNameEditText.setText(state.name)
                    }

                    if (state.description != binding.playListDescriptionEditText.text?.toString()) {
                        binding.playListDescriptionEditText.setText(state.description)
                    }

                    if (state.finish) {
                        finish()
                    }
                }
            }
        }
    }

    private fun updateImage(uri: Uri?) {
        val transform = RequestOptions().transform(CenterCrop(), RoundedCorners(16))
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.add_photo)
            .apply(transform)
            .into(binding.newPlayListImage)
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "${UUID.randomUUID()}.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        val uri = file.toUri()
        viewModel.setImage(uri)
    }

}