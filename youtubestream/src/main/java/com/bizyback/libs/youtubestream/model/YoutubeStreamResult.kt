package com.bizyback.libs.youtubestream.model

/**
 * @project : YoutubeStream
 * @author  : mambo
 * @email   : mambobryan@gmail.com
 * @date    : Wed 31 January 2024
 * @time    : 12:20 am
 */
sealed interface YoutubeStreamResult<out T> {
    data class Error(val message: String) : YoutubeStreamResult<Nothing>
    data class Success<T>(val data: T) : YoutubeStreamResult<T>
}