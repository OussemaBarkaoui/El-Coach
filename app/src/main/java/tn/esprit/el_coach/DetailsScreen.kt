package tn.esprit.el_coach

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DetailsScreen(navController: NavHostController, itemName: String, imageId: Int) {
    // Define the background and padding for the details screen
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Title: Display the name of the selected item
        Text(
            text = "Details for $itemName",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Item Image (adjusted size to fit properly)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp) // Adjusted height for better size
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray) // Placeholder for image
        ) {
            Image(
                painter = painterResource(id = imageId), // Use the passed image ID
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Ensures the image scales properly
            )
        }

        // Spacer between the image and the text content
        Spacer(modifier = Modifier.height(16.dp))

        // Description of the selected item
        Text(
            text = "$itemName is an amazing program designed to help you achieve your fitness goals. It includes various challenges, workouts, and nutrition tips to guide you to success. The program is structured to help you stay on track and motivated throughout the journey.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weeks/Duration for the item (can be dynamic)
        Text(
            text = "Program Duration: 12 weeks",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // A button to start the program (or some related action)
        Button(
            onClick = {
                // Handle the button click, e.g., start the program or go to another screen
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Start $itemName Now")
        }
    }
}

