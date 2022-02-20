package com.ryuk.cachingsample.domain.use_case.impl

import com.ryuk.cachingsample.base.BaseCachingUseCase
import com.ryuk.cachingsample.data.repository.MovieRepository
import com.ryuk.cachingsample.domain.model.MovieResponse
import com.ryuk.cachingsample.domain.use_case.FetchPopularMovieUseCase
import javax.inject.Inject

class FetchPopularMovieUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : FetchPopularMovieUseCase, BaseCachingUseCase<MovieResponse>() {

    override fun parseFromString(string: String): MovieResponse =
        gson.fromJson(string, MovieResponse::class.java)

    override suspend fun fetchDataFromNetwork() = movieRepository.fetchPopularMovies()

    override suspend fun fetchPopularMovies() = getDataFromStore()


}