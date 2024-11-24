import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import tn.esprit.el_coach.Routes
import tn.esprit.el_coach.ui.theme.login.LoginViewModel
import tn.esprit.el_coach.ui.theme.workout.WorkoutViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    navController: NavHostController, loginViewModel: LoginViewModel,
    isMenuExpanded: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val settingsMenuItems = listOf("Profile", "Privacy", "Help", "Logout")

    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = onDismiss
    ) {
        settingsMenuItems.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onDismiss()
                    when (item) {
                        "Logout" -> {
                             loginViewModel.logout(context)
                            navController.navigate(Routes.Login.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }

                        }
                        "Profile" -> {
                            // Handle profile navigation
                        }
                        "Privacy" -> {
                            // Handle privacy settings
                        }
                        "Help" -> {
                            // Handle help section
                        }
                    }
                },
                text = {
                    Text(
                        text = item,
                        style = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
            )
        }
    }
}

