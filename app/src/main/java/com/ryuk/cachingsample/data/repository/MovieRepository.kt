package com.ryuk.cachingsample.data.repository

import com.ryuk.cachingsample.base.model.RestClientResult
import com.ryuk.cachingsample.domain.model.MovieResponse

interface MovieRepository {

    suspend fun fetchPopularMovies(): RestClientResult<MovieResponse>

}