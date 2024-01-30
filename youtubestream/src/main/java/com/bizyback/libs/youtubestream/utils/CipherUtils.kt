package com.bizyback.libs.youtubestream.utils

import com.bizyback.libs.youtubestream.model.streams.Stream
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Tue 30 January 2024
 * @time    : 11:24 pm
 */
internal fun String.decodeUrl(): String =
    URLDecoder.decode(this, "UTF-8")

internal fun String.encodeUrl(): String =
    URLEncoder.encode(this, "UTF-8")

internal fun List<Stream>.encodeUrl(
    decodeSignatures: List<String>,
    encSignatures: Map<Int, String>
) = encSignatures.keys.zip(decodeSignatures).toMap().let { signatures ->
        filter { signatures.any { (key, _) -> it.streamDetails.itag == key } }
            .map { it to it.copy(url = it.url.plus("&sig=${signatures[it.streamDetails.itag]}")) }
            .unzip()
            .let { this - it.first.toSet() + it.second }
    }