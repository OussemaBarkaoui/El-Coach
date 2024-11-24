package tn.esprit.el_coach

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Response
import tn.esprit.el_coach.data.network.ApiService
import tn.esprit.el_coach.data.network.ForgotPasswordRequest
import tn.esprit.el_coach.data.network.ForgotPasswordResponse
import tn.esprit.el_coach.data.network.GoogleSignInRequest
import tn.esprit.el_coach.data.network.GoogleSignInResponse
import tn.esprit.el_coach.data.network.LoginRequest
import tn.esprit.el_coach.data.network.LoginResponse
import tn.esprit.el_coach.data.network.ResetPasswordRequest
import tn.esprit.el_coach.data.network.SignUpRequest
import tn.esprit.el_coach.data.network.SignUpResponse
import tn.esprit.el_coach.model.repositories.UserRepository
import tn.esprit.el_coach.ui.theme.El_CoachTheme
import tn.esprit.el_coach.ui.theme.login.LoginViewModel


@Composable
fun Home(navController: NavHostController, loginViewModel: LoginViewModel) {

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Welcome to Home Screen!")

        // Logout Button
        Button(onClick = {
            // Call the logout function
            loginViewModel.logout(context)

            // Navigate to the login screen after logging out
            navController.navigate(Routes.Login.route) {
                // Optional: Clear the back stack so the user can't navigate back
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }) {
            Text(text = "Logout")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    // Mock NavHostController
    val mockNavController = androidx.navigation.compose.rememberNavController()

    // Mock ApiService
    val mockApiService = object : ApiService {
        // Provide dummy implementations for the required ApiService methods
        override fun equals(other: Any?): Boolean {
            return super.equals(other)
        }

        fun someApiMethod() {
            // No-op or dummy behavior
        }

        override suspend fun signUp(signupRequest: SignUpRequest): Response<SignUpResponse> {
            TODO("Not yet implemented")
        }

        override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
            TODO("Not yet implemented")
        }

        override fun forgotPassword(request: ForgotPasswordRequest): Call<ForgotPasswordResponse> {
            TODO("Not yet implemented")
        }

        override fun resetPassword(request: ResetPasswordRequest): Call<Void> {
            TODO("Not yet implemented")
        }

        override suspend fun googleSignIn(request: GoogleSignInRequest): Response<GoogleSignInResponse> {
            TODO("Not yet implemented")
        }
    }

    // Create a mock UserRepository using the mock ApiService
    val mockUserRepository = UserRepository(mockApiService)

    // Mock LoginViewModel using the mock UserRepository
    val mockLoginViewModel = LoginViewModel(mockUserRepository)

    El_CoachTheme { // Use your app's theme
        Home(
            navController = mockNavController,
            loginViewModel = mockLoginViewModel
        )
    }
}

