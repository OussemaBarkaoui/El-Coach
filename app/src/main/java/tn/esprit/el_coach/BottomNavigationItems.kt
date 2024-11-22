package tn.esprit.el_coach

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String,
    val title: String,
    val icon: Any
) {
    object Home : BottomNavigationItems(
        route = "Home",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    object Diet : BottomNavigationItems(
        route = "Diet",
        title = "Diet",
        icon = R.drawable.baseline_restaurant_24
    )

    object Workout : BottomNavigationItems(
        route = "Workout",
        title = "Workout",
        icon = R.drawable.baseline_sports_gymnastics_24
    )

    object Stats : BottomNavigationItems(
        route = "Stats",
        title = "Stats",
        icon = R.drawable.baseline_query_stats_24
    )

    object Assistants : BottomNavigationItems(
        route = "Assistants",
        title = "Assist",
        icon = R.drawable.baseline_assistant_24
    )

    object Profile : BottomNavigationItems(
        route = "Profile",
        title = "Profile",
        icon = Icons.Outlined.Person
    )
}