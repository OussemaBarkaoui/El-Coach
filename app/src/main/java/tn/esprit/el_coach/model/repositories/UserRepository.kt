package tn.esprit.el_coach.model.repositories

import retrofit2.Response
import tn.esprit.el_coach.data.network.ApiService
import tn.esprit.el_coach.data.network.LoginRequest
import tn.esprit.el_coach.data.network.LoginResponse
import tn.esprit.el_coach.data.network.SignUpRequest
import tn.esprit.el_coach.data.network.SignUpResponse
import tn.esprit.el_coach.data.network.GoogleSignInRequest

class UserRepository(private val apiService: ApiService) {

    // Function to create a new user using the API with error handling
    suspend fun signUp(name: String, email: String, password: String, image: String, phoneNumber: Int): Response<SignUpResponse> {
        val signUpRequest = SignUpRequest(name, email, password, image, phoneNumber)
        return apiService.signUp(signUpRequest)
    }

    // Function to login a user
    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequest(email, password)
        return try {
            // Call the API to authenticate the user
            val response = apiService.login(loginRequest)

            // Handle successful response
            if (response.isSuccessful) {
                response
            } else {
                // Return error response in case of failure
                Response.error(response.code(), response.errorBody() ?: throw Exception("Login failed"))
            }
        } catch (exception: Exception) {
            throw Exception("Login failed: ${exception.message}")
        }
    }

    // Function to handle Google Sign-In
    suspend fun googleSignIn(idToken: String): Response<LoginResponse> {
        val googleSignInRequest = GoogleSignInRequest(idToken)
        return try {
            // Call the API to authenticate with Google
            val response = apiService.googleSignIn(googleSignInRequest)

            if (response.isSuccessful) {
                response
            } else {
                Response.error(
                    response.code(),
                    response.errorBody() ?: throw Exception("Google Sign-In failed")
                )
            }
        } catch (exception: Exception) {
            throw Exception("Google Sign-In failed: ${exception.message}")
        }
    }
}