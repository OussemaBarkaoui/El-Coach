package tn.esprit.el_coach

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tn.esprit.el_coach.data.network.RetrofitClient
import tn.esprit.el_coach.model.repositories.UserRepository
import tn.esprit.el_coach.ui.theme.forgetpassword.ForgetPassword
import tn.esprit.el_coach.ui.theme.forgetpassword.ResetPass
import tn.esprit.el_coach.ui.theme.login.Login
import tn.esprit.el_coach.ui.theme.login.LoginViewModel
import tn.esprit.el_coach.ui.theme.profile.Profile
import tn.esprit.el_coach.ui.theme.profile.ProfileViewModel

import tn.esprit.el_coach.ui.theme.signup.SignUp
import tn.esprit.el_coach.ui.theme.signup.SignUpViewModel
import tn.esprit.el_coach.ui.theme.workout.FullScreenVideoPlayer
import tn.esprit.el_coach.ui.theme.workout.VideoFeedScreen
import tn.esprit.el_coach.ui.theme.workout.Workout
import tn.esprit.el_coach.ui.theme.workout.WorkoutDetail


@Composable
fun NavigationGraph(navController: NavHostController, onBottomBarVisibilityChanged: (Boolean) -> Unit) {
        NavHost(navController, startDestination = Routes.LunchScreen.route) {

        composable(Routes.Home.route) {
            onBottomBarVisibilityChanged(true)
            val apiService = RetrofitClient.getApiService()
            val userRepository = UserRepository(apiService)
            val loginViewModel = LoginViewModel(userRepository)
            Home(navController, loginViewModel)
        }
        composable(Routes.LunchScreen.route) {
            onBottomBarVisibilityChanged(false)
            LunchScreen(navController)
        }

        composable(Routes.ForgetPassword.route) {
            onBottomBarVisibilityChanged(false)
            ForgetPassword(navController)
        }
        composable(Routes.Workout.route) {
            onBottomBarVisibilityChanged(false)
            Workout(navController)
        }
            composable("workoutDetail/{videoUrl}") { backStackEntry ->
                val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                WorkoutDetail(navController, videoUrl)
            }
            composable(
                route = "fullScreenVideo/{videoUrl}",
                arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoUrl = Uri.decode(backStackEntry.arguments?.getString("videoUrl") ?: "")

                FullScreenVideoPlayer(
                    navController = navController,
                    url = videoUrl,
                    onBackPress = { navController.popBackStack() }
                )
            }
            composable("videoFeed") {
                val context = LocalContext.current  // Get the current context

                // Create the video list with the correct URI for local resources and remote URLs
                val videoList = listOf(
                    Uri.parse("android.resource://${context.packageName}/${R.raw.workout1}").toString(),  // Local video from raw folder
                    Uri.parse("android.resource://${context.packageName}/${R.raw.workout2}").toString(),  // Remote video
                    Uri.parse("android.resource://${context.packageName}/${R.raw.workout1}").toString()  // Another remote video
                )

                VideoFeedScreen(
                    videoList = videoList,
                    navController = navController
                )
            }

            composable(Routes.ResetPass.route) {
            onBottomBarVisibilityChanged(false)
            ResetPass(navController)
        }
        composable(Routes.Login.route) {
            val apiService = RetrofitClient.getApiService()
            val userRepository = UserRepository(apiService)
            val loginViewModel = LoginViewModel(userRepository)
            onBottomBarVisibilityChanged(false)
            Login(navController,loginViewModel)
        }
        composable(Routes.SignUp.route) {
            val apiService = RetrofitClient.getApiService()
            val userRepository = UserRepository(apiService)
            val signUpViewModel = SignUpViewModel(userRepository)
            onBottomBarVisibilityChanged(false)
            SignUp(navController,signUpViewModel)
        }
            composable(Routes.Profile.route) {
                onBottomBarVisibilityChanged(false)
                val apiService = RetrofitClient.getApiService()
                val userRepository = UserRepository(apiService)
                val loginViewModel = LoginViewModel(userRepository)
                Profile(navController, loginViewModel)

            }

    }
}