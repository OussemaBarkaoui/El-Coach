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

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val hasNavigated: Boolean = false,
    val isGoogleSignInSuccess: Boolean = false
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        _loginUiState.value = LoginUiState()
    }

    fun handleGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _loginUiState.value?.let { currentState ->
                _loginUiState.value = currentState.copy(isLoading = true)
            }

            try {
                // Appel à votre API avec le token
                val response = userRepository.googleSignIn(idToken)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginUiState.value = loginResponse?.let {
                        LoginUiState(
                            isLoggedIn = true,
                            token = it.accessToken,
                            isGoogleSignInSuccess = true
                        )
                    } ?: LoginUiState(errorMessage = "Google Sign-In failed: Empty response")
                } else {
                    _loginUiState.value = LoginUiState(errorMessage = "Google Sign-In failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState(errorMessage = e.message ?: "Google Sign-In failed")
            }
        }
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        viewModelScope.launch {
            try {
                _loginUiState.value = LoginUiState(isLoading = true)

                val account = completedTask.getResult(ApiException::class.java)
                val idToken = account?.idToken

                if (idToken != null) {
                    handleGoogleSignIn(idToken)
                } else {
                    _loginUiState.value = LoginUiState(errorMessage = "Google Sign-In failed: ID Token is null")
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
                _loginUiState.value = LoginUiState(errorMessage = "Google Sign-In failed: ${e.statusCode}")
            }
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
            _loginUiState.value?.let { currentState ->
                _loginUiState.value = currentState.copy(isLoading = true)
            }

            try {
                val response: Response<LoginResponse> = userRepository.login(email, password)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginUiState.value = loginResponse?.let {
                        LoginUiState(isLoggedIn = true, token = it.accessToken)
                    } ?: LoginUiState(errorMessage = "Login failed: Empty response body")
                } else {
                    _loginUiState.value = LoginUiState(errorMessage = "Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState(errorMessage = e.message ?: "An unknown error occurred")
            }
        }
    }
}
