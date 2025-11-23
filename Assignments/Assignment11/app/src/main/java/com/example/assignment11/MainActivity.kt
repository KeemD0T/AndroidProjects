package com.example.assignment11


import androidx.compose.ui.graphics.Color


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.assignment11.ui.theme.Assignment11Theme

/**
 * MainActivity is the entry point of the app.
 * - Sets up the Compose UI and applies the MaterialTheme.
 * - Configures the status bar for better visibility.
 * - Displays a photo gallery in a grid layout.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set status bar icons to dark for visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                PhotoGallery()
            }
        }
    }
}

/**
 * Displays a simple photo gallery using a grid layout.
 * - Uses LazyVerticalGrid for efficient scrolling and grid arrangement.
 * - Each cell is a Card with a centered label.
 */



/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    PhotoGallery()
}
