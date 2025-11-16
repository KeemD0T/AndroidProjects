package com.example.assignment10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.assignment10.ui.theme.Assignment10Theme


// MainActivity is the entry point of the Android application.
class MainActivity : ComponentActivity() {
    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons for a modern look.
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Initialize the ViewModel using by viewModels.
        // The provideFactory method ensures the ViewModel has access to the application context.
        val viewModel: NoteViewModel by viewModels {
            NoteViewModel.provideFactory(application)
        }

        // Set up the Compose UI content for this activity.
        setContent {
            // Apply the custom theme for the application.
            MaterialTheme {
                // Surface is a fundamental composable that applies background color and fills the screen.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Display the NoteScreen, passing the initialized ViewModel to it.
                    NoteScreen(viewModel)
                }
            }
        }
    }
}