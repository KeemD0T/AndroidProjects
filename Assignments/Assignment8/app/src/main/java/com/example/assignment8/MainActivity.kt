package com.example.assignment8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.assignment8.ui.theme.Assignment8Theme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewModel using the by viewModels delegate.
        // It calls the provideFactory method that is correctly defined in your ContactViewModel.
        val viewModel: ContactViewModel by viewModels {
            ContactViewModel.provideFactory(application)
        }

        setContent {
            Assignment8Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call the ContactScreen and pass the viewModel instance.
                    // If this line is red, it means ContactScreen is not imported or
                    // its package declaration is wrong.
                    ContactScreen(viewModel = viewModel)
                }
            }
        }
    }
}