package com.bizyback.libs.youtubestream.model

import com.bizyback.libs.youtubestream.model.streams.Stream

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:22 pm
 */
data class VideoData(
    val videoDetails: VideoDetails,
    val streams: List<Stream>
)