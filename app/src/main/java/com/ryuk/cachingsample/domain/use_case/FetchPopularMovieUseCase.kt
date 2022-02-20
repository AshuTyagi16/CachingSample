package com.ryuk.cachingsample.domain.use_case

import com.dropbox.android.external.store4.StoreResponse
import com.ryuk.cachingsample.base.model.RestClientResult
import com.ryuk.cachingsample.domain.model.MovieResponse
import kotlinx.coroutines.flow.Flow

interface FetchPopularMovieUseCase {

    suspend fun fetchPopularMovies(): Flow<StoreResponse<RestClientResult<MovieResponse>>>

}