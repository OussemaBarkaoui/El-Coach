package tn.esprit.el_coach.ui.theme.signup

import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import tn.esprit.el_coach.Greeting
import tn.esprit.el_coach.R
import tn.esprit.el_coach.Routes
import tn.esprit.el_coach.ui.theme.El_CoachTheme
import tn.esprit.el_coach.ui.theme.login.Login
import tn.esprit.el_coach.ui.theme.login.SocialButtonWithIcon


@Composable
fun SignUp(navController: NavHostController = rememberNavController(), // Default value for preview
           signUpViewModel: SignUpViewModel = viewModel()) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isNameValid = remember { mutableStateOf(true) }
    val isEmailValid = remember { mutableStateOf(true) }
    val isPasswordValid = remember { mutableStateOf(true) }
    val profilePicUri = remember { mutableStateOf("") }
    val signUpUiState by signUpViewModel.signUpUiState.observeAsState(SignUpUiState())
    val phoneNumber = remember { mutableStateOf(0) } // Default value set to 0
    val isPhoneNumberValid = remember { mutableStateOf(true) }
    val isImageValid = remember { mutableStateOf(true) } // Validation flag for image

    // Validation function for phone number
    fun validatePhoneNumber(input: Int): Boolean {
        val inputString = input.toString()
        return inputString.isNotBlank() && inputString.length >= 8 // Adjust length as needed
    }
    fun validateImage(input: String): Boolean {
        return input.isNotBlank() // An image is valid if the URI is not empty
    }
    // Validation functions
    fun validateEmail(input: String): Boolean {
        return input.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun validateName(input: String): Boolean {
        return input.isNotBlank()
    }

    fun validatePassword(input: String): Boolean {
        val hasUpperCase = input.any { it.isUpperCase() }
        val hasLowerCase = input.any { it.isLowerCase() }
        val hasNumber = input.any { it.isDigit() }
        return input.isNotBlank() && hasUpperCase && hasLowerCase && hasNumber
    }
    val backgroundColor = Color(0xFFFFF2DC)
    val iconAndLabelColor = Color(0xFF2A4E62)
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val buttonColor = Color(0xFF001C2F)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePicUri.value = uri?.toString() ?: "" // Update state with selected image URI or reset to default
        isImageValid.value = validateImage(profilePicUri.value)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backk),
            contentDescription = null,
            alpha = 0.6f,
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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
                    text = "Sign Up",
                    fontSize = 32.sp,
                    color = iconAndLabelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }



            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { imagePickerLauncher.launch("image/*") } // Open image picker
            ) {
                val imagePainter = if (profilePicUri.value.isNotBlank()) {
                    rememberAsyncImagePainter(profilePicUri.value) // Load selected image URI
                } else {
                    painterResource(id = R.drawable.user) // Use default image resource
                }

                Image(
                    painter = imagePainter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.height(12.dp))

            // Name Text Field
            TextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                    isNameValid.value = validateName(it)
                },
                label = { Text("FullName", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "FullName",
                        tint = iconAndLabelColor
                    )
                },
                isError = !isNameValid.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isNameValid.value) iconAndLabelColor else Color.Red,
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

            if (!isNameValid.value) {
                Text(
                    text = "FullName cannot be empty",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Email Text Field
            TextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    isEmailValid.value = validateEmail(it)
                },
                label = { Text("Email", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email",
                        tint = iconAndLabelColor
                    )
                },
                isError = !isEmailValid.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isEmailValid.value) iconAndLabelColor else Color.Red,
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

            if (!isEmailValid.value) {
                Text(
                    text = "Enter a valid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password Text Field
            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    isPasswordValid.value = validatePassword(it)
                },
                label = { Text("Password", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password",
                        tint = iconAndLabelColor
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = !isPasswordValid.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isPasswordValid.value) iconAndLabelColor else Color.Red,
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

            if (!isPasswordValid.value) {
                Text(
                    text = "Password must contain uppercase, lowercase, and a number",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = if (it != password.value) "Passwords do not match" else null
                },
                label = { Text("Confirm Password", color = iconAndLabelColor) },
                isError = confirmPasswordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (confirmPasswordError != null) Color.Red else iconAndLabelColor,
                        RoundedCornerShape(12.dp)
                    ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Confirm Password",
                        tint = iconAndLabelColor
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(12.dp))


            confirmPasswordError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }



            TextField(
                value = if (phoneNumber.value == 0) "" else phoneNumber.value.toString(), // Show empty if default value
                onValueChange = { input ->
                    // Handle invalid input gracefully
                    val numericValue = input.toIntOrNull()
                    if (numericValue != null) {
                        phoneNumber.value = numericValue
                        isPhoneNumberValid.value = validatePhoneNumber(numericValue)
                    } else if (input.isEmpty()) {
                        phoneNumber.value = 0
                        isPhoneNumberValid.value = false
                    }
                },
                label = { Text("Phone Number", color = iconAndLabelColor) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Phone Number",
                        tint = iconAndLabelColor
                    )
                },
                isError = !isPhoneNumberValid.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isPhoneNumberValid.value) iconAndLabelColor else Color.Red,
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

            if (!isPhoneNumberValid.value) {
                Text(
                    text = "Phone Number must be at least 8 digits",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Handle sign-up button click
            Button(
                onClick = {
                    if (isNameValid.value && isEmailValid.value && isPasswordValid.value) {
                        signUpViewModel.signUpUser(name.value, email.value, password.value, profilePicUri.value, phoneNumber.value)
                        navController.navigate(Routes.Login.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4E62)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Sign Up")
            }


            Text(
                "Already have an account? Log in",
                color = Color.Black,  // Use contrasting color for readability
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,  // Make the text bold
                modifier = Modifier
                    .padding(top = 16.dp) // Adjust top padding to ensure it’s visible
                    .align(Alignment.CenterHorizontally)
                    .clickable{navController.navigate(Routes.Login.route)}
            )

            // Show success or error message based on sign-up result
            signUpUiState.successMessage?.let {
                Text(
                    text = it,
                    color = Color.Green,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                )
            }

            signUpUiState.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                )
            }

            // Navigate to next screen after successful sign-up
            if (signUpUiState.isSignedUp && !signUpUiState.hasNavigated) {
                navController.navigate(Routes.Login.route) // Replace with actual route
            }

        }

    }

}



