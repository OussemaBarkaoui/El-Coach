package tn.esprit.el_coach.ui.theme.forgetpassword

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.el_coach.Routes
import tn.esprit.el_coach.data.network.ResetPasswordRequest
import tn.esprit.el_coach.data.network.RetrofitClient

@Composable
fun ResetPass(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Reset Your Password",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.White
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = email.isEmpty(),
                textStyle = TextStyle(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Verification Code TextField
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Verification Code", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = code.isEmpty(),
                textStyle = TextStyle(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password TextField
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = newPassword.isEmpty(),
                textStyle = TextStyle(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    // Reset message when a request is sent
                    message = ""

                    if (email.isNotEmpty() && code.isNotEmpty() && newPassword.isNotEmpty()) {
                        isLoading = true
                        // Send API request to reset password
                        RetrofitClient.getApiService().resetPassword(request = ResetPasswordRequest(email, code, newPassword))
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    isLoading = false
                                    if (response.isSuccessful) {
                                        message = "Password reset successfully!"
                                        navController.navigate(Routes.Login.route) // Navigate back after success
                                    } else {
                                        message = "Error: ${response.message()}"
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    isLoading = false
                                    message = "Error: ${t.localizedMessage}"
                                    Log.e("ResetPass", "Error occurred: ${t.localizedMessage}", t)
                                }
                            })
                    } else {
                        message = "Please fill all fields."
                    }
                },
                enabled = !isLoading
            ) {
                Text(text = if (isLoading) "Resetting..." else "Reset Password", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display any messages after the request
            if (message.isNotEmpty()) {
                Text(text = message, color = Color.White)
            }
        }
    }

}
