package tn.esprit.el_coach.ui.theme.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePicUri.value = uri?.toString() ?: "" // Update state with selected image URI or reset to default
        isImageValid.value = validateImage(profilePicUri.value)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.back2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 132.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ensure the Box takes the full width for proper alignment
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = "Join Us !",
                    color = Color(0xFF000000),
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center) // Center-align the text within the Box
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
                    painterResource(id = R.drawable.img) // Use default image resource
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



            // Name Text Field
            OutlinedTextField(
                value = name.value,
                label = { Text(text = "FullName") },
                onValueChange = {
                    name.value = it
                    isNameValid.value = validateName(it)
                },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "FullName") },
                isError = !isNameValid.value,
                placeholder = { Text(text = "FullName") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if (!isNameValid.value) {
                Text(
                    text = "FullName cannot be empty",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }


            // Email Text Field
            OutlinedTextField(
                value = email.value,
                label = { Text(text = "Email") },
                onValueChange = {
                    email.value = it
                    isEmailValid.value = validateEmail(it)
                },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email") },
                isError = !isEmailValid.value,
                placeholder = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if (!isEmailValid.value) {
                Text(
                    text = "Enter a valid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }

            // Password Text Field
            OutlinedTextField(
                value = password.value,
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    password.value = it
                    isPasswordValid.value = validatePassword(it)
                },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = "Password") },
                isError = !isPasswordValid.value,
                placeholder = { Text(text = "Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if (!isPasswordValid.value) {
                Text(
                    text = "Password must contain uppercase, lowercase, and a number",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }

            if (!isPasswordValid.value) {
                Text(
                    text = "Password must contain uppercase, lowercase, and a number",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }
            OutlinedTextField(
                value = if (phoneNumber.value == 0) "" else phoneNumber.value.toString(), // Show empty if default value
                label = { Text(text = "Phone Number") },
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
                leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Phone Number") },
                isError = !isPhoneNumberValid.value,
                placeholder = { Text(text = "Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            if (!isPhoneNumberValid.value) {
                Text(
                    text = "Phone Number must be at least 8 digits",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
                )
            }
            // Handle sign-up button click
            Button(
                onClick = {
                    if (isNameValid.value && isEmailValid.value &&  isPhoneNumberValid.value && isImageValid.value) {
                        // Trigger sign-up in ViewModel
                        signUpViewModel.signUpUser(name.value, email.value, password.value,profilePicUri.value,phoneNumber.value)
                        navController.navigate(Routes.Login.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2980B9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Sign Up")
            }

            Text(
                "Already have an account? Log in",
                color = Color(0xFF2980B9),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 89.dp)
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



