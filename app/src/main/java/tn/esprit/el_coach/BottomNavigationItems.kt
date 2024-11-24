package tn.esprit.el_coach

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.common.api.Api
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person


sealed class BottomNavigationItems (

    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null

){
    object Home : BottomNavigationItems(
        route = "Home",
        title = "Home",
        icon = Icons.Outlined.Home
    )
    object Workout : BottomNavigationItems(
        route = "Workout",
        title = "Workout",
        icon = Icons.Outlined.FitnessCenter
    )

    object Reminder : BottomNavigationItems(
        route = "Reminder",
        title = "Reminder",
        icon = Icons.Outlined.Notifications
    )



    object Favorite : BottomNavigationItems(
        route = "Favorite",
        title = "Favorite",
        icon = Icons.Outlined.Favorite
    )


    object Profile : BottomNavigationItems(
        route = "Profile",
        title = "Profile",
        icon = Icons.Outlined.Person
    )
}