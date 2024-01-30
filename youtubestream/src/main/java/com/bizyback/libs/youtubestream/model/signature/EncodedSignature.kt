package com.bizyback.libs.youtubestream.model.signature

import com.bizyback.libs.youtubestream.model.streams.StreamDetails

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:19 pm
 */
data class EncodedSignature(
    val url: String,
    val signature: String,
    val streamDetails: StreamDetails
)