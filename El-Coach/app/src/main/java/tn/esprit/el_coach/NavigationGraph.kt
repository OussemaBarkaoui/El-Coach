package tn.esprit.el_coach

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import tn.esprit.el_coach.data.network.RetrofitClient
import tn.esprit.el_coach.model.repositories.UserRepository
import tn.esprit.el_coach.ui.theme.forgetpassword.ForgetPassword
import tn.esprit.el_coach.ui.theme.forgetpassword.ResetPass
import tn.esprit.el_coach.ui.theme.login.Login
import tn.esprit.el_coach.ui.theme.login.LoginViewModel
import tn.esprit.el_coach.ui.theme.signup.SignUp
import tn.esprit.el_coach.ui.theme.signup.SignUpViewModel


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
    }
}