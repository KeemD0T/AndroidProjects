package com.example.assignment9

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.assignment9.MovieRepository
import com.example.assignment9.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    imdbId: String,
    navController: NavController,
    movieRepository: MovieRepository
) {
    val movieViewModel: MovieViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MovieViewModel(movieRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )
    
    var movieDetails by remember { mutableStateOf<com.example.assignment9.api.OmdbMovieDetailResponse?>(null) }
    val isLoading by movieViewModel.isLoading.collectAsState()
    val error by movieViewModel.error.collectAsState()

    LaunchedEffect(imdbId) {
        movieDetails = movieViewModel.getMovieDetailsById(imdbId)
    }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search for a movie") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Search Button
            Button(
                onClick = {
                    navController.navigate("movieSearch")
                },
                enabled = searchQuery.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display loading indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            // Display error message
            error?.let { errorMessage ->
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Display movie details
            movieDetails?.let { movie ->
                Text(
                    text = movie.title ?: "Unknown Title",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                movie.poster?.let { posterUrl ->
                    if (posterUrl != "N/A") {
                        AsyncImage(
                            model = posterUrl,
                            contentDescription = "Movie Poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                movie.year?.let {
                    Text(
                        text = "Year: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.rated?.let {
                    Text(
                        text = "Rated: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.runtime?.let {
                    Text(
                        text = "Runtime: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.genre?.let {
                    Text(
                        text = "Genre: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.director?.let {
                    Text(
                        text = "Director: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.actors?.let {
                    Text(
                        text = "Actors: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                movie.plot?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Plot: $it",
                        style = MaterialTheme.typography.bodyMedium
                    //style = MaterialTheme.typography.bodyMedium
                    )
                }

                
                movie.imdbRating?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "IMDB Rating: $it",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } ?: if (!isLoading && error == null) {
                Text(
                    text = "Loading movie details...",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "No movie details available.",
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
    }
}
