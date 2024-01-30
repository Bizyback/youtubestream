package com.bizyback.libs.youtubestream

import android.content.Context
import com.bizyback.libs.youtubestream.model.VideoData
import com.bizyback.libs.youtubestream.model.VideoDetails
import com.bizyback.libs.youtubestream.model.YoutubeStreamResult

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Wed 31 January 2024
 * @time    : 12:17 am
 */
object YoutubeStream {

    private var ytStream: YTStream? = null
    private val base = "https://www.youtube.com/watch?v="

    fun init(context: Context) {
        ytStream = YTStream(context = context)
    }

    suspend fun getVideoDataById(id: String): YoutubeStreamResult<VideoData> {
        val stream = ytStream
            ?: return YoutubeStreamResult.Error(message = "YoutubeStream is not initialized, please initialize to continue \nRun `YoutubeStream.init(context)` to initialize")
        val result = stream.getVideoDataById(id = id)
        if (result.isFailure) {
            val error = result.exceptionOrNull()?.localizedMessage
                ?: "Unable to get video details by id -> id = $id"
            return YoutubeStreamResult.Error(message = error)
        }
        val data = result.getOrNull()
            ?: return YoutubeStreamResult.Error(message = "VideoData for id ($id) was null")
        return YoutubeStreamResult.Success(data = data)
    }

    suspend fun getVideoDetailsById(id: String): YoutubeStreamResult<VideoDetails> {
        val stream = ytStream
            ?: return YoutubeStreamResult.Error(message = "YoutubeStream is not initialized, please initialize to continue \nRun `YoutubeStream.init(context)` to initialize")
        val result = stream.getVideoDetailsById(id = id)
        if (result.isFailure) {
            val error = result.exceptionOrNull()?.localizedMessage
                ?: "Unable to get video details by id -> id = $id"
            return YoutubeStreamResult.Error(message = error)
        }
        val data = result.getOrNull()
            ?: return YoutubeStreamResult.Error(message = "VideoDetails for id ($id) was null")
        return YoutubeStreamResult.Success(data = data)
    }

    suspend fun getVideoDetailsByLink(link: String): YoutubeStreamResult<VideoDetails> {
        if (link.contains(base).not())
            return YoutubeStreamResult.Error(message = "Invalid youtube link : $link")
        val id = link.replace(base, "")
        return getVideoDetailsById(id = id)
    }

    suspend fun getVideoDataByLink(link: String): YoutubeStreamResult<VideoData> {
        if (link.contains(base).not())
            return YoutubeStreamResult.Error(message = "Invalid youtube link : $link")
        val id = link.replace(base, "")
        return getVideoDataById(id = id)
    }

}