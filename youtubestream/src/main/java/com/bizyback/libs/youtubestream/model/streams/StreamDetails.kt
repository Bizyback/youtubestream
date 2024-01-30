package com.bizyback.libs.youtubestream.model.streams

import com.bizyback.libs.youtubestream.model.codec.AudioCodec
import com.bizyback.libs.youtubestream.model.codec.VideoCodec

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:21 pm
 */
data class StreamDetails(
    val itag: Int,
    val type: StreamType,
    val extension: Extension,
    val audioCodec: AudioCodec? = null,
    val videoCodec: VideoCodec? = null,
    val quality: Int? = null,
    val bitrate: Int? = null,
    val fps: Int? = if (type == StreamType.AUDIO) null else 30,
    val expiresInSeconds: Long? = null
)