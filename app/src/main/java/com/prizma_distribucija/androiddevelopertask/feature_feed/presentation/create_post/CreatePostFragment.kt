package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.create_post

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePostViewModel by viewModels()

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreatePostBinding.bind(view)
        viewModel

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.fabTakePicture.setOnClickListener {
            onCameraClick()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createPostEvents.collectLatest { event ->
                    when (event) {
                        is CreatePostViewModel.CreatePostEvents.ShowMessage -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createPostStatus.collectLatest { status ->
                    when (status) {
                        is Resource.Error -> {
                            handleCreatePostError(status.message ?: "An unknown error occurred.")
                        }
                        is Resource.Loading -> {
                            handleCreatePostLoading()
                        }
                        is Resource.Success -> {
                            handleCreatePostSuccess("Successfully saved")
                        }
                    }
                }
            }
        }
    }

    private fun handleCreatePostLoading() {
        binding.progressBarCreatePost.visibility = View.VISIBLE
    }

    private fun handleCreatePostError(message: String) {
        binding.progressBarCreatePost.visibility = View.GONE
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleCreatePostSuccess(message: String) {
        binding.progressBarCreatePost.visibility = View.GONE
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun onImageCaptured(image: ImageProxy) {
        val bitmap = viewModel.convertImageToBitmap(image) ?: return
        val fileName = System.currentTimeMillis().toString()
        val file = File(requireContext().cacheDir, fileName)
        file.createNewFile()

        viewModel.createPost(bitmap, file)
        changeUiOnImageCaptured(bitmap)
    }

    private fun changeUiOnImageCaptured(bitmap: Bitmap) =
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            binding.previewView.visibility = View.GONE
            binding.imageViewCaptured.setImageBitmap(bitmap)
            binding.imageViewCaptured.visibility = View.VISIBLE
        }

    private fun onCameraClick() {
        imageCapture.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)

            }

            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onImageCaptured(image)
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder()
            .setBufferFormat(ImageFormat.YUV_420_888)
            .setTargetRotation(requireView().display.rotation)
            .build()


        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            imageCapture,
            preview
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}