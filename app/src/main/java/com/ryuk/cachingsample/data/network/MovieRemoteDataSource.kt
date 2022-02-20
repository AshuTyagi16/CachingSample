package com.ryuk.cachingsample.data.network

import com.ryuk.cachingsample.base.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(
    private val movieService: MovieService
) : BaseDataSource() {

    suspend fun fetchPopularMovies() = getResult {
        movieService.fetchPopularMovies()
    }

}