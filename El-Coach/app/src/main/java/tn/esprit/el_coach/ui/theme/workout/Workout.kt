package tn.esprit.el_coach.ui.theme.workout

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import tn.esprit.el_coach.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun Workout(
    navController: NavHostController = rememberNavController(), // Default value for preview
    workoutViewModel: WorkoutViewModel? = null // Mock ViewModel for default value
) {
    var isVideoPlaying by remember { mutableStateOf(false) }


    // Sample video list (use proper URIs for video paths)
    val context = LocalContext.current
    val videoList = listOf(
        VideoItem("Push Day - Chest & Triceps", "45 mins", Uri.parse("android.resource://${context.packageName}/${R.raw.workout1}").toString()),
        VideoItem("Leg Day - Squats & Deadlifts", "60 mins", Uri.parse("android.resource://${context.packageName}/${R.raw.workout2}").toString()),
        VideoItem("Cardio - High-Intensity Intervals", "30 mins", Uri.parse("android.resource://${context.packageName}/${R.raw.workout1}").toString())
    )
    var currentVideoUrl by remember { mutableStateOf(videoList.first().url) }

    LaunchedEffect(Unit) {
        if (currentVideoUrl == null && videoList.isNotEmpty()) {
            currentVideoUrl = videoList[0].url
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Featured Video Section
        // Display the current video (if playing)
        VideoPlayer(url = videoList.first().url) // Or use state to manage the selected video



        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Workout Videos",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2A4E62),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // List of Workout Plans with Video URLs
        LazyColumn {
            items(videoList) { video ->
                WorkoutPlanCard(
                    title = video.title,
                    duration = video.duration,
                    videoUrl = video.url,
                    onVideoClick = { _ ->
                        navController.navigate("videoFeed")
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Button to Track Progress
        Button(
            onClick = { /* Navigate to Progress Page */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC9A3))
        ) {
            Text(text = "Track Your Progress", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WorkoutPlanCard(title: String, duration: String, videoUrl: String, onVideoClick: (String) -> Unit) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .clickable { onVideoClick(videoUrl) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display Video Thumbnail using Glide
            AndroidView(
                factory = { ImageView(context) },
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                update = { imageView ->
                    Glide.with(context)
                        .load(videoUrl) // Load video URL
                        .apply(RequestOptions().frame(1000)) // Grab a frame at 1 second
                        .into(imageView)
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Workout Title and Duration
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB1BAC7)
                )
            }
        }
    }
}




data class VideoItem(val title: String, val duration: String, val url: String)
data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val username: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likes: Int = 0
)

data class VideoInteractionState(
    val likes: Int = 0,
    val comments: List<Comment> = emptyList(),
    val rating: Float = 0f,
    val isLiked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoInteractionOverlay(
    state: VideoInteractionState,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Like Button
        InteractionButton(
            icon = if (state.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            count = state.likes,
            isSelected = state.isLiked,
            onClick = onLikeClick
        )

        // Comment Button
        InteractionButton(
            icon = Icons.Filled.Comment,
            count = state.comments.size,
            onClick = onCommentClick
        )

        // Share Button
        InteractionButton(
            icon = Icons.Filled.Share,
            onClick = onShareClick
        )

        // Rating Display
        RatingDisplay(rating = state.rating)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSection(
    comments: List<Comment>,
    onCommentSubmit: (String) -> Unit,
    onCommentLike: (String) -> Unit
) {
    var newCommentText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Comment Input
        OutlinedTextField(
            value = newCommentText,
            onValueChange = { newCommentText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Add a comment...") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (newCommentText.isNotEmpty()) {
                            onCommentSubmit(newCommentText)
                            newCommentText = ""
                        }
                    }
                ) {
                    Icon(Icons.Filled.Send, "Send")
                }
            }
        )

        // Comments List
        LazyColumn {
            items(comments) { comment ->
                CommentItem(comment = comment, onLike = { onCommentLike(comment.id) })
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, onLike: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = formatTimestamp(comment.timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = comment.content)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Like",
                        tint = Color.Gray
                    )
                }
                Text(
                    text = "${comment.likes}",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RatingDisplay(rating: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun InteractionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .scale(scale)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.Red else Color.White
            )
        }
        if (count != null) {
            Text(
                text = formatCount(count),
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count < 1000 -> count.toString()
        count < 1000000 -> String.format("%.1fK", count / 1000f)
        else -> String.format("%.1fM", count / 1000000f)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(timestamp))
    }
}
@Preview(showBackground = true)
@Composable
fun WorkoutPreview() {
    Workout()
}
