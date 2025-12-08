package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.navigation.AppNavigation
import com.example.finalproject.screens.HomeScreen
import com.example.finalproject.ui.theme.FinalProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalProjectTheme {
                // Create the NavController here
                val navController = rememberNavController()
                // Pass it to your AppNavigation
                AppNavigation(navController = navController)
            }
            }
        }
    }




//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    FinalProjectTheme {
//        HomeScreen(
//        )
//
//    }
//}