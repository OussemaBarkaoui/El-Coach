package tn.esprit.el_coach.ui.theme.profile

import SettingsMenu
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import tn.esprit.el_coach.ui.theme.login.LoginViewModel
import tn.esprit.el_coach.ui.theme.workout.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavHostController ,loginViewModel: LoginViewModel,
    profileViewModel: WorkoutViewModel? = null,
    profileImageUrl: String = "https://via.placeholder.com/80",
    profileName: String = "Julia Smith"
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {

                },
                actions = {
                    IconButton(onClick = { /* TODO: Show Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    // Toggle the dropdown menu
                    IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    SettingsMenu(
                        isMenuExpanded = isMenuExpanded,
                        onDismiss = { isMenuExpanded = false },
                        navController = navController,
                        loginViewModel = loginViewModel
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Profile Picture and Name
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray, CircleShape)
                ) {
                    // Use Coil or other library to load an image
                    Image(
                        painter = rememberImagePainter(profileImageUrl),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(profileName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weight Section
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("WEIGHT", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = 0.75f, // Example progress
                        color = Color.Green,
                        trackColor = Color.Gray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start\n68kg", color = Color.White, textAlign = TextAlign.Center)
                        Text("Current\n56kg", color = Color.White, textAlign = TextAlign.Center)
                        Text("Target\n52kg", color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons Section
            Column {
                Button(
                    onClick = { /* TODO: Add weight logging functionality */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Log weight", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProfileOption("Edit profile", onClick = { /* TODO: Edit profile action */ })
                ProfileOption("Saved workouts", onClick = { /* TODO: Show saved workouts */ })
                ProfileOption("History", onClick = { /* TODO: Show workout history */ })
            }
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onClick() }, // Clickable for action
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}
