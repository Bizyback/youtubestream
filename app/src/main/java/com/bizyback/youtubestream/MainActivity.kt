package com.bizyback.youtubestream

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bizyback.libs.youtubestream.YoutubeStream
import com.bizyback.libs.youtubestream.models.YoutubeStreamResult
import com.bizyback.youtubestream.ui.theme.YoutubeStreamTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val TAG = "PettyTest"

    private val _state = MutableStateFlow<String?>(null)
    private val url: StateFlow<String?> = _state

    private suspend fun getStreamUrl() {
        when (val result = YoutubeStream.getVideoDetailsById(id = "SaneSRqePVY")) {
            is YoutubeStreamResult.Error -> {
                Log.d(TAG, "getStreamUrl: failed -> ${result.message}")
            }

            is YoutubeStreamResult.Success -> {
                _state.value = result.data.url
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                scope.launch { getStreamUrl() }
            }
            val state by url.collectAsState()
            YoutubeStreamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AnimatedContent(
                            modifier = Modifier.fillMaxSize(),
                            targetState = state,
                            label = ""
                        ) { value ->
                            when (value) {
                                null -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Fetching")

                                    }
                                }

                                else -> {
                                    VideoPlayer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(400.dp), url = value
                                    )
                                }
                            }

                        }

                    }
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("OpaqueUnitKey")
@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri(url)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setDisplayTitle("Playing")
                .build()
        )
        .build()

    // create our player
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            this.setMediaItems(listOf(mediaItem))
            this.prepare()
            this.playWhenReady = true
        }
    }

    Box(modifier = modifier) {
        val (title, videoPlayer) = createRefs()

        // video title
        Text(
            text = "Current Title",
            color = Color.White,
            modifier =
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )

        // player view
        DisposableEffect(
            AndroidView(
                modifier =
                Modifier
                    .testTag("VideoPlayer")
                    .fillMaxWidth(),
                factory = {

                    // exo player view for our video player
                    PlayerView(context).apply {
                        player = exoPlayer
                        controllerAutoShow = false
                        layoutParams =
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT,
                                ViewGroup.LayoutParams
                                    .MATCH_PARENT
                            )
                    }
                }
            )
        ) {
            onDispose {
                // release player when no longer needed
                exoPlayer.release()
            }
        }
    }
}