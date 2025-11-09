package com.example.assignment9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assignment9.ui.theme.Assignment9Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment9Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val movieRepository = MovieRepository()

                    NavHost(navController = navController, startDestination = "movieSearch") {
                        composable("movieSearch") {
                            MovieSearchScreen(
                                navController = navController,
                                movieViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                                        override fun <T : androidx.lifecycle.ViewModel> create(
                                            modelClass: Class<T>
                                        ): T {
                                            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                                                @Suppress("UNCHECKED_CAST")
                                                return MovieViewModel(movieRepository) as T
                                            }
                                            throw IllegalArgumentException("Unknown ViewModel class")
                                        }
                                    }
                                )
                            )
                        }

                        composable(
                            route = "movie_details/{imdbId}",
                            arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val imdbId = backStackEntry.arguments?.getString("imdbId")
                            if (imdbId != null) {
                                MovieDetailScreen(
                                    imdbId = imdbId,
                                    navController = navController,
                                    movieRepository = movieRepository
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
