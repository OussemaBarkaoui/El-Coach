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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.el_coach.Routes
import tn.esprit.el_coach.data.network.ResetPasswordRequest
import tn.esprit.el_coach.data.network.RetrofitClient

@Composable
fun ResetPass(navController: NavController) {
    var resetToken by remember { mutableStateOf(0) }
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


            // Verification Code TextField
            OutlinedTextField(
                value = resetToken.toString(),  // Convert the integer to string for display
                onValueChange = {
                    // Only allow numeric input and update the code
                    resetToken = it.toIntOrNull() ?: 0
                },
                label = { Text("Verification Code", color = Color.Black) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = resetToken == 0,  // Show error if the code is 0 (initial state)
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password TextField
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password", color = Color.Black) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = newPassword.isEmpty(),
                textStyle = TextStyle(color = Color.Black)
            )
            println(resetToken)
            Spacer(modifier = Modifier.height(16.dp))
            // Submit Button
            Button(
                onClick = {
                    if (resetToken!=0 && newPassword.isNotEmpty()) {
                        isLoading = true
                        println(resetToken)
                        RetrofitClient.getApiService().resetPassword(
                            ResetPasswordRequest(resetToken, newPassword)
                        ).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    message = "Password updated successfully!"
                                    navController.navigate(Routes.Login.route)
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("ResetPassword", "Failed: ${response.message()}, Error: $errorBody")
                                    message = "Failed: ${response.message()}"
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                isLoading = false
                                Log.e("ResetPassword", "Error: ${t.localizedMessage}")
                                message = "Error: ${t.localizedMessage}"
                            }
                        })
                    } else {
                        message = "Please enter a valid 4-digit code and new password."
                    }
                },
                enabled = !isLoading
            ) {
                Text(text = if (isLoading) "Resetting..." else "Reset Password", color = Color.White)
            }
            println(resetToken)
            Spacer(modifier = Modifier.height(16.dp))

            // Display any messages after the request
            if (message.isNotEmpty()) {
                Text(text = message, color = Color.White)
            }
        }
    }

}
