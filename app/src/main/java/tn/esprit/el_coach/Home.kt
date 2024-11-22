package tn.esprit.el_coach

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tn.esprit.el_coach.ui.theme.login.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@Composable
fun Home(navController: NavHostController, loginViewModel: LoginViewModel) {

    Scaffold(
//        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome Back, Champion..!!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Progress Section
            ProgressSection()

            Spacer(modifier = Modifier.height(24.dp))


            // Weeks Complete Section
            // Weeks Complete Section
            WeeksCompleteSection(navController = navController)
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun ProgressSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "view all >",
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(110.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                        
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Training Level-2",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "3/6 sessions",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(110.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Weight",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "55kgs",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(110.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Calorie",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "504 cal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun WeeksCompleteSection(navController: NavHostController) {
    // Create a scrollable state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState) // Enable vertical scrolling
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommendations",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "view all >",
                color = Color.Gray
            )
        }

        // Cards section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Transformation Card
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(170.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clickable {
                            // Navigate to details screen with the title "Transformation"
                            navController.navigate("details/Transformation/img_1")
                        },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Transformation",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "4 weeks",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // LiveFit Card
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(170.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clickable {
                            // Navigate to details screen with the title "LiveFit"
                            navController.navigate("details/LiveFit/livefit")
                        },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.livefit),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LiveFit",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "12 weeks",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    subtitle: String,
    gradient: List<Color>
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient[0])
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun WeeksCard(
    title: String,
    weeks: String,
    gradient: List<Color>
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient[0])
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = weeks,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

//@Composable
//fun BottomNavigationBar() {
//    NavigationBar {
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
//            label = { Text("Home") },
//            selected = true,
//            onClick = { }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Progress") },
//            label = { Text("Progress") },
//            selected = false,
//            onClick = { }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Nutrition") },
//            label = { Text("Nutrition") },
//            selected = false,
//            onClick = { }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//            label = { Text("Profile") },
//            selected = false,
//            onClick = { }
//        )
//    }
//
//}

//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//    device = "spec:width=411dp,height=891dp"
//)
//@Composable
//fun FitnessHomeScreenPreview() {
//    MaterialTheme {
//        Home()
//    }
//}



