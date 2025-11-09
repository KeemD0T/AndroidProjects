package com.example.assignment9.api
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
interface OmdApiService {


    @GET("/")
    suspend fun searchMovies(
        @Query("s") searchQuery: String,
        @Query("apikey") apiKey: String
    ): MovieSearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
        ): OmdbMovieDetailResponse

}
