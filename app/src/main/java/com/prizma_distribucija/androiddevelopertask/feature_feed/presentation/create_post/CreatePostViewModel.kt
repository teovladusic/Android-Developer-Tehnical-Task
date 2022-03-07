package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.create_post

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.core.util.toBitmap
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.*
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val createPostEventsChannel = Channel<CreatePostEvents>()
    val createPostEvents = createPostEventsChannel.receiveAsFlow()

    private val _createPostStatus = MutableSharedFlow<Resource<Post>>()
    val createPostStatus = _createPostStatus.asSharedFlow()

    @SuppressLint("UnsafeOptInUsageError")
    fun createPost(bitmap: Bitmap, file: File) = viewModelScope.launch(dispatcherProvider.io) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitmapData = bos.toByteArray()

        val bitmapFile = saveBitmapToFile(file, bitmapData)

        createPostUseCase(bitmapFile).collectLatest {
            _createPostStatus.emit(it)
        }
    }

    private fun saveBitmapToFile(file: File, bitmapData: ByteArray): File {
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            sendCreatePostEvent(CreatePostEvents.ShowMessage("Error saving the message."))
        }

        try {
            if (fos == null) {
                throw IOException()
            }

            fos.apply {
                write(bitmapData)
                flush()
                close()
            }

        } catch (e: IOException) {
            sendCreatePostEvent(CreatePostEvents.ShowMessage("Error saving the message."))
        }

        return file
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun convertImageToBitmap(image: ImageProxy): Bitmap? {
        if (image.image == null) return null
        if (image.format != ImageFormat.YUV_420_888) {
            sendCreatePostEvent(CreatePostEvents.ShowMessage("krivi format"))
        }
        return image.image!!.toBitmap()
    }

    private fun sendCreatePostEvent(event: CreatePostEvents) =
        viewModelScope.launch {
            createPostEventsChannel.send(event)
        }

    sealed class CreatePostEvents {
        data class ShowMessage(val message: String) : CreatePostEvents()
    }
}