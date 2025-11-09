package com.example.assignment9



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment9.api.MovieSearchItem
import com.example.assignment9.api.MovieSearchResponse
import com.example.assignment9.api.OmdbMovieDetailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MovieSearchItem>>(emptyList())
    val searchResults: StateFlow<List<MovieSearchItem>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchMovies() {
        _isLoading.value = true
        _error.value = null
        _searchResults.value = emptyList()

        viewModelScope.launch {
            val query = _searchQuery.value
            if (query.isBlank()) {
                _error.value = "Search query cannot be empty"
                _isLoading.value = false
                return@launch
            }
            repository.searchMovies(query)
                .onSuccess { response ->
                    if (response.response == "True" && response.search != null) {
                        _searchResults.value = response.search
                    } else {
                        _error.value = response.error ?: "No movies found for that title."
                    }
                }
                .onFailure { e ->
                    _error.value = e.message ?: "An unknown error occurred."
                }
            _isLoading.value = false
        }
    }

    suspend fun getMovieDetailsById(imdbId: String): OmdbMovieDetailResponse? {
        _isLoading.value = true
        _error.value = null

        val result = repository.getMovieDetails(imdbId)
        _isLoading.value = false

        return result.getOrElse { e ->
            _error.value = e.message ?: "An error occurred fetching movie details."
            null
        }.takeIf { it?.response == "True" }
    }

}

