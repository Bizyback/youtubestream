package com.bizyback.libs.youtubestream

import android.util.Log
import com.bizyback.libs.youtubestream.helpers.NewPipeDownloaderImpl
import com.bizyback.libs.youtubestream.models.YoutubeStreamResult
import com.bizyback.libs.youtubestream.models.YoutubeVideoDetails
import com.bizyback.libs.youtubestream.models.safeExecution
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Wed 31 January 2024
 * @time    : 12:17 am
 */
object YoutubeStream {

    private val TAG = "YoutubeStreamLib"
    private val base = "https://www.youtube.com/watch?v="

    init {
        NewPipe.init(NewPipeDownloaderImpl())
        NewPipe.getService(ServiceList.YouTube.serviceId)
    }

    suspend fun getVideoDetailsById(id: String): YoutubeStreamResult<YoutubeVideoDetails> =
        getVideoDetails(url = "$base$id")

    suspend fun getVideoDetailsByLink(url: String): YoutubeStreamResult<YoutubeVideoDetails> =
        getVideoDetails(url = url)

    private suspend fun getVideoDetails(url: String) = safeExecution {
        val streamInfo = StreamInfo.getInfo(url)
        if (streamInfo.videoStreams.isNullOrEmpty()) throw Exception("No video stream info found")
        streamInfo.videoStreams.forEach {
            Log.i(TAG,
                buildString {
                    append("-".repeat(10))
                    append(" STREAM ")
                    append("-".repeat(10))
                    append("\n")
                    append("FPS: ${it.fps} \n")
                    append("Codec: ${it.codec} \n")
                    append("Format: ${it.format} \n")
                    append("Quality: ${it.quality} \n")
                    append("Bitrate: ${it.bitrate} \n")
                    append("Resolution: ${it.getResolution()} \n")
                    append("Delivery Method : ${it.deliveryMethod} \n")
                }
            )
        }
        val bestVideoStream = streamInfo.videoStreams
            .filterNotNull()
            .filter { it.content != null }
            .sortedByDescending { it.quality }
            .first()
        if (bestVideoStream.content == null) throw Exception("No video stream content found")
        YoutubeVideoDetails(url = bestVideoStream.content, fps = bestVideoStream.fps)
    }

}