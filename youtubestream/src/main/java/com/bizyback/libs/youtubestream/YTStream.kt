package com.bizyback.libs.youtubestream

import android.content.Context
import com.bizyback.libs.youtubestream.extractor.StreamExtractor
import com.bizyback.libs.youtubestream.model.VideoData
import com.bizyback.libs.youtubestream.model.VideoDetails
import com.bizyback.libs.youtubestream.model.toVideoDetails
import com.bizyback.libs.youtubestream.model.youtube.InitialPlayerResponse
import com.bizyback.libs.youtubestream.network.NetworkService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.util.regex.Pattern

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:25 pm
 */
internal class YTStream(context: Context) {

    private val streamExtractor = StreamExtractor(context)

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json(builderAction = {
        explicitNulls = false
        ignoreUnknownKeys = true
    })

    suspend fun getVideoDataById(id: String): Result<VideoData> =
        NetworkService.getVideoPage(id).mapCatching { pageHtml ->
            val playerResponse =
                patternPlayerResponse.matcher(pageHtml).takeIf { it.find() }?.group(1)
                    ?: throw IllegalStateException()

            val ytPlayerResponse = json.decodeFromString<InitialPlayerResponse>(playerResponse)
            val streamingData = ytPlayerResponse.streamingData

            val videoDetails = ytPlayerResponse.toVideoDetails()

            val formats = (streamingData.formats + streamingData.adaptiveFormats)
                .filter { it.type != "FORMAT_STREAM_TYPE_OTF" }

            val streams = streamExtractor.extractStreams(pageHtml, formats)
            VideoData(videoDetails, streams)
        }

    suspend fun getVideoDetailsById(id: String): Result<VideoDetails> =
        NetworkService.getVideoPage(id).mapCatching { pageHtml ->
            val playerResponse =
                patternPlayerResponse.matcher(pageHtml).takeIf { it.find() }?.group(1)
                    ?: throw IllegalStateException()

            val ytPlayerResponse = json.decodeFromString<InitialPlayerResponse>(playerResponse)
            ytPlayerResponse.toVideoDetails()
        }

    companion object {
        private val patternPlayerResponse =
            Pattern.compile("var ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\s*;")
    }
}