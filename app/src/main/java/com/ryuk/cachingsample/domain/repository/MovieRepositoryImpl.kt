package com.ryuk.cachingsample.domain.repository

import com.ryuk.cachingsample.data.network.MovieRemoteDataSource
import com.ryuk.cachingsample.data.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun fetchPopularMovies() = movieRemoteDataSource.fetchPopularMovies()
}