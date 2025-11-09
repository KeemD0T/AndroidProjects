package com.example.assignment9

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.assignment9.api.MovieSearchItem
import com.example.assignment9.api.OmdbMovieDetailResponse
import com.example.assignment9.api.RetrofitInstance
import androidx.navigation.NavController



@Composable
fun MovieSearchScreen(navController: NavController, movieViewModel: MovieViewModel = viewModel()) {
    val searchQuery by movieViewModel.searchQuery.collectAsState()
    val searchResults by movieViewModel.searchResults.collectAsState()
    val isLoading by movieViewModel.isLoading.collectAsState()
    val error by movieViewModel.error.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top=50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { movieViewModel.onSearchQueryChange(it) },
                label = { Text("Movie Title") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { movieViewModel.searchMovies() }) {
                    Text("Search")
                }
            }
        }

        item {
            if (isLoading) {
                CircularProgressIndicator()
            }
            error?.let { errorMsg ->
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
        }

        // Display search results
        items(searchResults) { movie ->
            MovieSearchItemCard(movie = movie, onMovieClick = { imdbId ->
                navController.navigate("movie_details/${imdbId}")
            })
        }
    }
}

@Composable
fun MovieSearchItemCard(movie: MovieSearchItem, onMovieClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { movie.imdbID?.let {imdbId -> onMovieClick(imdbId)} } // Click to show details
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(

                model = movie.poster,
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .height(100.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = movie.title ?: "No title provide",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = movie.year ?: "No movie provided", style = MaterialTheme.typography
                    .bodySmall)
            }
        }
    }
}

