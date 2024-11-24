package tn.esprit.el_coach.ui.theme.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import retrofit2.Response
import tn.esprit.el_coach.data.network.LoginResponse
import tn.esprit.el_coach.model.repositories.UserRepository

import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import tn.esprit.el_coach.data.network.GoogleSignInRequest
import kotlin.math.log

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false,
    val isGoogleSignInSuccess: Boolean = false
)

class LoginViewModel(private val userRepository: UserRepository,) : ViewModel() {

    private val _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        _loginUiState.value = LoginUiState()
    }

    fun handleGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            // Set loading state
            _loginUiState.value = _loginUiState.value?.copy(isLoading = true)
            Log.d("GoogleIDToken", "ID Token: $idToken")

            try {
                // Create the GoogleSignInRequest object
                val request = GoogleSignInRequest(idToken)
                val response = userRepository.googleSignIn(request)

                Log.d("GoogleSignIn", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginUiState.value = LoginUiState(
                        isLoggedIn = true,
                        token = loginResponse?.accessToken
                    )
                } else {
                    Log.e("GoogleSignIn", "Error: ${response.message()}")
                    _loginUiState.value = LoginUiState(
                        errorMessage = "Google Sign-In failed: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Exception: ${e.message}", e)
                _loginUiState.value = LoginUiState(
                    errorMessage = e.message ?: "An error occurred during Google Sign-In"
                )
            } finally {
                // Reset loading state
                _loginUiState.value = _loginUiState.value?.copy(isLoading = false)
            }
        }
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Get the signed-in account
            val account = completedTask.getResult(ApiException::class.java)
            // Successfully signed in, use the account
            Log.d("GoogleSignIn", "Signed in as: ${account.email}")

            // Call handleGoogleSignIn with the ID token
            account.idToken?.let { idToken ->
                handleGoogleSignIn(idToken) // Pass the ID token to your ViewModel
            } ?: run {
                Log.e("GoogleSignIn", "ID Token is null")
                _loginUiState.value = LoginUiState(
                    errorMessage = "Google Sign-In failed: ID Token is null"
                )
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.statusCode}")
            // Handle sign-in failure
            _loginUiState.value = LoginUiState(
                errorMessage = "Google Sign-In failed with status: ${e.statusCode}"
            )
        }
    }

    private fun saveUserInfo(context: Context, email: String?, displayName: String?) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("email", email)
            putString("displayName", displayName)
            apply()
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            Log.d("LoginDebug", "Login started with email: $email")
            _loginUiState.value = LoginUiState(isLoading = true)

            try {
                val response: Response<LoginResponse> = userRepository.login(email, password)
                Log.d("LoginDebug", "Response received: $response")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("LoginDebug", "Login successful: $loginResponse")
                        _loginUiState.value = LoginUiState(isLoggedIn = true, token = loginResponse.accessToken)
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
