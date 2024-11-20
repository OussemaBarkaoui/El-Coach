package tn.esprit.el_coach.ui.theme.login

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import tn.esprit.el_coach.BottomNavigationItems
import tn.esprit.el_coach.R
import tn.esprit.el_coach.Routes
import java.util.regex.Pattern

@Composable
fun Login(navController: NavHostController, viewModel: LoginViewModel = viewModel(),modifier: Modifier = Modifier,) {

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val checked = remember { mutableStateOf(sharedPreferences.getBoolean("RememberMe", false)) }
    var emailErrorMessage by remember { mutableStateOf("") }
    // Check if RememberMe is true and if credentials are saved
    val savedEmail = sharedPreferences.getString("Email", "")
    val savedPassword = sharedPreferences.getString("Password", "")

    if (checked.value && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
        // If "Remember Me" is checked and credentials are found, navigate to Home directly
        LaunchedEffect(true) {
            navController.navigate(BottomNavigationItems.Home.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }


    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("883402420156-a7c8u8f588ok0no3dm5mlodseul1h0ku.apps.googleusercontent.com") // Remplacez par votre client ID
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    // Appeler la méthode de viewModel pour gérer la connexion Google
                    viewModel.handleGoogleSignIn(token)
                }
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
            }
        }
    }

    val emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$")
    val loginUiState by viewModel.loginUiState.observeAsState(LoginUiState())

    // Flags to track focus on each text field
    var emailFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val backgroundColor = Color(0xFFFFF2DC)
    val iconAndLabelColor = Color(0xFF2A4E62)
    val buttonColor = Color(0xFF001C2F)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.backk),
            contentDescription = "Background",
            alpha = 0.6f,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .border(2.dp, iconAndLabelColor, RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
                    .padding(0.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 32.sp,
                    color = iconAndLabelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                label = { Text("Email", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "Email Icon",
                        tint = iconAndLabelColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (emailError) Color.Red else iconAndLabelColor,
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                },
                label = { Text("Password", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Password Icon",
                        tint = iconAndLabelColor
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = iconAndLabelColor
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (passwordError) Color.Red else iconAndLabelColor,
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)  // Padding for the row
                    .fillMaxWidth(),  // Ensures the row fills the available width
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start  // Align the contents to the left
            ) {
                Checkbox(
                    checked = checked.value,
                    onCheckedChange = {
                        checked.value = it
                        if (!it) {
                            // If unchecked, clear saved credentials
                            sharedPreferences.edit().clear().apply()
                        }
                    }
                )
                Text(
                    text = "Remember me",
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    Log.d("LoginDebug", "Email: $email, Password: $password")
                    // Trigger login action
                    viewModel.loginUser(email, password)

                    // If Remember Me is checked, save the email and password
                    if (checked.value) {
                        sharedPreferences.edit().apply {
                            putString("Email", email)
                            putString("Password", password)
                            putBoolean("RememberMe", true)
                            apply()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4E62)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Login")
            }

            // Loading Indicator
            if (loginUiState.isLoading) {
                CircularProgressIndicator()
            }

            // Show Snackbar on successful login
            if (loginUiState.isLoggedIn) {
                LaunchedEffect(loginUiState.isLoggedIn) {
                    if (loginUiState.isLoggedIn) {
                        snackbarHostState.showSnackbar("Login successful! Welcome back.")
                        navController.navigate(BottomNavigationItems.Home.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }


            // Forgot password link
            Text(
                "Forgot password",
                color = Color.Black,  // Use contrasting color for readability
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,  // Make the text bold
                modifier = Modifier
                    .padding(start = 16.dp)  // Add small padding for the left side
                    .clickable(onClick = {
                        navController.navigate(Routes.ForgetPassword.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            )


            Text(
                "Don’t have an account? Join us",
                color = Color.Black,  // Use contrasting color for readability
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,  // Make the text bold
                modifier = Modifier
                    .padding(top = 16.dp) // Adjust top padding to ensure it’s visible
                    .align(Alignment.CenterHorizontally) // Align it horizontally at the center
                    .clickable {
                        navController.navigate(Routes.SignUp.route) {

                        }
                    }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .width(160.dp)
                    .background(color = Color(0xFFB1BAC7), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 12.dp)
                    .clickable {
                        launcher.launch(googleSignInClient.signInIntent)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google Sign In",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(18.dp)

                )
                Text(
                    "Google",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }
    }
}


@Composable
fun SocialButtonWithIcon(label: String, iconRes: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .width(160.dp)
            .background(color = Color(0xFFB1BAC7), shape = RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(18.dp)
        )
        Text(
            label,
            color = Color.White,
            fontSize = 16.sp,
        )
    }
}
