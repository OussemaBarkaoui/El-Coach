package tn.esprit.el_coach.ui.theme.workout

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = true
        }
    }

    AndroidView(
        factory = { context ->
            StyledPlayerView(context).apply {
                player = exoPlayer
                useController = false // Disable progress bar, play/pause button, etc.
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
fun WorkoutDetail(navController: NavHostController, videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                StyledPlayerView(it).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f)
        )
    }
}
@Composable
fun FullScreenVideoPlayer(navController: NavHostController, url: String, onBackPress: () -> Unit) {
    val context = LocalContext.current

    if (url.isEmpty()) {
        // Handle invalid URL
        Text(text = "Error: Video URL is missing.", color = Color.Red)
        return
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { StyledPlayerView(it).apply { player = exoPlayer } },
            modifier = Modifier.fillMaxSize()
        )

        // Back button
        IconButton(
            onClick = { onBackPress() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun VideoFeedScreen(videoList: List<String>, navController: NavHostController) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    var showComments by remember { mutableStateOf(false) }

    // Store video data
    var videosData by remember {
        mutableStateOf(
            videoList.map { url ->
                VideoData(url = url)
            }
        )
    }

    // Create and manage players for each video
    val players = remember {
        videoList.map { videoUrl ->
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                prepare()
            }
        }
    }

    // Handle page changes and video playback
    LaunchedEffect(pagerState.currentPage) {
        players.forEachIndexed { index, player ->
            if (index == pagerState.currentPage) {
                player.playWhenReady = true
                player.play()
            } else {
                player.pause()
            }
        }
    }

    // Clean up players when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            players.forEach { player ->
                player.release()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            count = videoList.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Video Player
                AndroidView(
                    factory = { context ->
                        StyledPlayerView(context).apply {
                            player = players[page]
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Interaction buttons
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                videosData = videosData.toMutableList().apply {
                                    val currentVideo = get(page)
                                    set(page, currentVideo.copy(
                                        isLiked = !currentVideo.isLiked,
                                        likes = if (currentVideo.isLiked)
                                            currentVideo.likes - 1
                                        else
                                            currentVideo.likes + 1
                                    ))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (videosData[page].isLiked)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Filled.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (videosData[page].isLiked) Color.Red else Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "${videosData[page].likes}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Comment Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { showComments = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Comment,
                                contentDescription = "Comment",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "${videosData[page].comments.size}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }

    // Comments Bottom Sheet
    if (showComments) {
        ModalBottomSheet(
            onDismissRequest = { showComments = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            CommentSection(
                comments = videosData[pagerState.currentPage].comments,
                onCommentSubmit = { commentText ->
                    val newComment = Comment(
                        userId = "current_user_id",
                        username = "User",
                        content = commentText
                    )
                    videosData = videosData.toMutableList().apply {
                        val currentVideo = get(pagerState.currentPage)
                        set(pagerState.currentPage, currentVideo.copy(
                            comments = currentVideo.comments + newComment
                        ))
                    }
                },
                onCommentLike = { /* Implement comment like functionality */ }
            )
        }
    }
}

data class VideoData(
    val url: String,
    val likes: Int = 0,
    val isLiked: Boolean = false,
    val comments: List<Comment> = emptyList()
)





