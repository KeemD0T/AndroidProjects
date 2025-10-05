package com.example.assignment4

import android.os.Bundle
import android.view.Display
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * This project covers concepts from Chapter 7 lessons:
 * - "Validation" - for form validation and error handling
 * - "Managing Input State" - for state management in forms
 * - "Text Fields" - for input field styling and error states
 * - "Regular Expressions" - for email, phone, and ZIP code validation
 *
 * Students should review these lessons before starting:
 * - Validation lesson for form validation patterns
 * - Managing Input State lesson for state management
 * - Text Fields lesson for input field styling
 * - Regular Expressions lesson for validation patterns
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactValidatorApp()
            }
        }
    }
}

@Composable
fun ContactValidatorApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ContactForm()
    }
}

@Composable
fun ContactForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }

    // TODO: Create state variables for form fields
    // Hint: You need variables for:
    // - name (string for user's name)
    // - email (string for email address)
    // - phone (string for phone number)
    // - zipCode (string for ZIP code)
    // Use remember and mutableStateOf for each
    // See "Validation" lesson for examples of state management

    var isUsernameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var isZipValid by remember { mutableStateOf(true) }
    // TODO: Create validation state variables
    // Hint: You need boolean variables for:
    // - isNameValid, isEmailValid, isPhoneValid, isZipValid
    // Use remember and mutableStateOf for each
    // See "Managing Input State" lesson for examples of validation state management

    var submittedInfo by remember { mutableStateOf("") }
    // TODO: Create submitted information state variable
    // Hint: You need a variable for: submittedInfo (string for displaying submitted data)
    // Use remember and mutableStateOf

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
                // TODO: Apply a RoundedCornerShape with a reasonable dp value
                // See "Text Fields" lesson for examples of shape customization
            )
            .padding(horizontal = 16.dp, vertical = 24.dp),
            // TODO: Add horizontal and vertical padding with a reasonable dp value
            // See "Text Fields" lesson for examples of padding
        /* TODO: Provide a reasonable value for horizontal padding */
       /* TODO: Provide a reasonable value for vertical padding */

    // TODO: Arrange items vertically with a reasonable dp spacing
    // See "Text Fields" lesson for examples of vertical arrangement
    verticalArrangement = Arrangement.spacedBy(16.dp),
    )/* TODO: Provide a reasonable value for vertical spacing */
    {
        NameField(
            name = name,
            isNameValid = isUsernameValid,
            onValueChange = {
                name = it
                isUsernameValid = it.isNotEmpty()
            }

        )
        EmailField(
            email = email,
            isEmailValid = isEmailValid,
            onValueChange = {
                email = it
                isEmailValid = validateEmail(it)
            }
        )
        PhoneField(
            phone = phone,
            isPhoneValid = isPhoneValid,
            onValueChange = {
                phone = it
                isPhoneValid = validatePhone(it)
            }
        )
        ZipCodeField(
            zipCode = zipCode,
            isZipValid = isZipValid,
            onValueChange = {
                zipCode = it
                isZipValid = validateZipCode(it)
            }
        )
        Button(
            onClick = {
                submittedInfo = "Name: $name\nEmail: $email\nPhone: $phone\nZIP Code: $zipCode"
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isUsernameValid && isEmailValid && isPhoneValid && isZipValid &&
                    name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && zipCode.isNotEmpty()

        ) {
            Text("Submit")
        }
        SubmittedInfoDisplay(
            submittedInfo = submittedInfo
        )
    }



        // TODO: Call the NameField composable here
        // Pass the name state, validation state, and onValueChange lambda
        // See "Text Fields" lesson for examples of error state styling

        // TODO: Call the EmailField composable here
        // Pass the email state, validation state, and onValueChange lambda
        // See "Validation" lesson for email validation examples

        // TODO: Call the PhoneField composable here
        // Pass the phone state, validation state, and onValueChange lambda
        // See "Regular Expressions" lesson for phone number validation patterns

        // TODO: Call the ZipCodeField composable here
        // Pass the zipCode state, validation state, and onValueChange lambda
        // See "Regular Expressions" lesson for ZIP code validation examples

        // TODO: Create the Submit button
        // Use Button composable with enabled state based on all validations
        // The button should only be enabled when all fields are valid and not empty
        // See "Validation" lesson for examples of complex button state management

        // TODO: Add display for submitted information
    }


@Composable
fun NameField(
    name: String,
    isNameValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
    )
    // TODO: Create the name input field
    //
    // See "Text Fields" lesson for complete examples of error state styling
}

@Composable
fun EmailField(
    email: String,
    isEmailValid: Boolean,
    onValueChange: (String) -> Unit
) {
    //val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

    OutlinedTextField(
        value = email,
        onValueChange = onValueChange ,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        isError = !isEmailValid && email.isNotEmpty(), // Show error if the parent says it's invalid
        supportingText = {
            if (!isEmailValid) {
                Text("Please enter a valid email address") // Display a generic error message
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}
    // TODO: Create the email input field
    //
    // See "Validation" lesson for email validation examples with regex


@Composable
fun PhoneField(
    phone: String,
    isPhoneValid: Boolean,
    onValueChange: (String) -> Unit
) {
   // val phoneRegex = "^(\\d{3}[- ]?)?\\d{3}[- ]?\\d{4}\$".toRegex()

    OutlinedTextField(
        value = phone,
        onValueChange = onValueChange,
        label = { Text("Phone") },
        modifier = Modifier.fillMaxWidth(),
        isError = !isPhoneValid && phone.isNotEmpty(), // Show error if the parent says it's invalid
        supportingText = {
            if (!isPhoneValid) {
                Text("Please enter a valid phone number") // Display a generic error message
            }
        },

        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        )
    )
    // TODO: Create the phone input field
    // Use OutlinedTextField with:
    //
    // See "Regular Expressions" lesson for phone number validation patterns and examples
}

@Composable
fun ZipCodeField(
    zipCode: String,
    isZipValid: Boolean,
    onValueChange: (String) -> Unit
) {
    //val zipRegex = "^\\d{5}\$".toRegex()
    OutlinedTextField(
        value = zipCode,
        onValueChange = onValueChange,
        label = { Text("ZIP Code") },
        modifier = Modifier.fillMaxWidth(),
    isError = !isZipValid && zipCode.isNotEmpty(), // Show error if the parent says it's invalid
    supportingText = {
        if (!isZipValid) {
            Text("Please enter a valid ZIP code") // Display a generic error message
        }
    },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}
 // TODO: Create the ZIP code input field
    // Use OutlinedTextField with:
    //
    // See "Regular Expressions" lesson for ZIP code validation examples


fun validateEmail(email: String): Boolean {
    // This regex checks for a username, an '@', a domain name, a '.', and a top-level domain.
    val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}\$".toRegex()
    return email.matches(emailRegex)
}

fun validatePhone(phone: String): Boolean {
    // Regex for formats like 123-456-7890, 123 456 7890, 1234567890, (123)-456-7890 etc.
    // This one specifically looks for 10 digits, optionally separated by hyphens or spaces.
    val phoneRegex = "^(\\d{3}[- ]?)?\\d{3}[- ]?\\d{4}\$".toRegex()
    return phone.matches(phoneRegex)
}
fun validateZipCode(zipCode: String): Boolean {
    // Regex to ensure exactly 5 digits
    val zipRegex = "^\\d{5}\$".toRegex()
    return zipCode.matches(zipRegex)
}
//submitButton: (
//        name: String,
//        email: String,
//        phone: String,
//        zipCode: String
//        ) -> Unit
@Composable
fun SubmittedInfoDisplay(submittedInfo: String) {
    // Only show this section if there is information to display
    if (submittedInfo.isNotEmpty()) {
        Text(
            text = submittedInfo,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 16.dp) // Add space above the text
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(4.dp))
                .padding(16.dp) // Add padding inside the background
        )
    }
}



// TODO: Create validation functions using regex
// Hint: You need three functions:
// 1. validateEmail() - checks email format using regex pattern
//

// 2. validatePhone() - checks for phone numbers like 123-456-7890 or 123/456/7890
//
// 3. validateZipCode() - checks for exactly 5 digits
//
// Use the .matches() function with regex patterns
// See "Regular Expressions" lesson for complete regex examples and validation patterns

// You will need to enable the submit button when all the fields are valid:
//
// See "Validation" lesson for detailed examples of complex button state management
//when button is clicked and all fields are valid and not empty, the submitted information should be displayed
//in a text field below the button.

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactValidatorAppPreview() {
    ContactValidatorApp()
}