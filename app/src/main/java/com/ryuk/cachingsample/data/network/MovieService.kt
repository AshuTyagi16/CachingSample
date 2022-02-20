package com.ryuk.cachingsample.data.network

import com.ryuk.cachingsample.domain.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    @GET("movie/popular?api_key=5e27dee69c9d0351682c4fc9124e8980")
    suspend fun fetchPopularMovies(): Response<MovieResponse>

}

