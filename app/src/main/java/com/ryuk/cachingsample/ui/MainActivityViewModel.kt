package com.ryuk.cachingsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.StoreResponse
import com.ryuk.cachingsample.base.model.RestClientResult
import com.ryuk.cachingsample.domain.model.MovieResponse
import com.ryuk.cachingsample.domain.use_case.FetchPopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchPopularMovieUseCase: FetchPopularMovieUseCase
) : ViewModel() {

    private val _moviesLiveData = MutableLiveData<StoreResponse<RestClientResult<MovieResponse>>>()
    val moviesLiveData: LiveData<StoreResponse<RestClientResult<MovieResponse>>>
        get() = _moviesLiveData

    fun fetchPopularMovies() {
        viewModelScope.launch {
            fetchPopularMovieUseCase.fetchPopularMovies().collect {
                _moviesLiveData.postValue(it)
            }
        }
    }
}