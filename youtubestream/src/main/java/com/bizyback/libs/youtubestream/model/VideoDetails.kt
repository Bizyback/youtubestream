package com.bizyback.libs.youtubestream.model

import com.bizyback.libs.youtubestream.model.youtube.InitialPlayerResponse

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:23 pm
 */
data class VideoDetails(
    val id: String,
    val title: String,
    val channel: String,
    val channelId: String,
    val description: String,
    val durationSeconds: Long,
    val expiresInSeconds: Long,
    val viewCount: Long,
    val thumbnails: List<Thumbnail>,
    val isLiveStream: Boolean
)

internal fun InitialPlayerResponse.toVideoDetails() = VideoDetails(
    id = videoDetails.videoId,
    title = videoDetails.title,
    channel = videoDetails.author,
    channelId = videoDetails.channelId,
    description = videoDetails.shortDescription,
    durationSeconds = videoDetails.lengthSeconds.toLong(),
    expiresInSeconds = streamingData.expiresInSeconds.toLong(),
    viewCount = videoDetails.viewCount.toLong(),
    thumbnails = videoDetails.thumbnail.thumbnails.map {
        Thumbnail(it.width, it.height, it.url)
    },
    isLiveStream = videoDetails.isLiveContent
)