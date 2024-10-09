package com.bizyback.libs.youtubestream.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface YoutubeStreamResult<out T> {
    data class Error(val message: String) : YoutubeStreamResult<Nothing>
    data class Success<T>(val data: T) : YoutubeStreamResult<T>
}

suspend fun <T> safeExecution(block: suspend () -> T): YoutubeStreamResult<T> =
    withContext(Dispatchers.IO) {
        try {
            YoutubeStreamResult.Success(block())
        } catch (e: Exception) {
            YoutubeStreamResult.Error(e.message ?: e.localizedMessage ?: "Unknown error")
        }
    }
