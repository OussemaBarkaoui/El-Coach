package tn.esprit.el_coach

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.common.api.Api


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
}