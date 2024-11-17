package tn.esprit.el_coach.ui.theme.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import tn.esprit.el_coach.data.network.LoginResponse
import tn.esprit.el_coach.model.repositories.UserRepository

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LoginDebug", "Login started with email: $email")
            _loginUiState.value = LoginUiState(isLoading = true) // Set loading state

            try {
                val response: Response<LoginResponse> = userRepository.login(email, password)
                Log.d("LoginDebug", "Response received: $response")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("LoginDebug", "Login successful: $loginResponse")

                        val accessToken = loginResponse.accessToken
                        _loginUiState.value = LoginUiState(isLoggedIn = true, token = accessToken)

                        // Save refreshToken or userId if needed
                        val refreshToken = loginResponse.refreshToken
                        val userId = loginResponse.userId
                        Log.d("LoginDebug", "Access Token: $accessToken, Refresh Token: $refreshToken, User ID: $userId")
                    } else {
                        Log.e("LoginDebug", "Login failed: Empty response body")
                        _loginUiState.value = LoginUiState(errorMessage = "Login failed: Empty response body")
                    }
                } else {
                    Log.e("LoginDebug", "Login failed: ${response.message()}")
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginDebug", "Login error: ${e.message}", e)
                _loginUiState.value = LoginUiState(errorMessage = e.message ?: "An unknown error occurred")
            }
        }
    }
}
