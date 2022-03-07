package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val createPostRepository: CreatePostRepository
) {

    suspend operator fun invoke(file: File) = flow<Resource<Post>> {
        emit(Resource.Loading())
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("upload", file.name, requestFile)
        val response = createPostRepository.createPost(body)

        if (response.isSuccessful) {
            emit(Resource.Success(response.data))
        } else {
            emit(Resource.Error(response.errorMessage ?: "Unknown error appeared."))
        }
    }
}