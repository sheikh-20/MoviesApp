package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.MovieFavourite
import com.application.moviesapp.domain.usecase.MovieFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(private val useCase: MovieFavouriteUseCase): ViewModel() {

    private var _movieFavourite = MutableStateFlow<Resource<List<MovieFavourite>>>(Resource.Loading)
    val movieFavourite get() = _movieFavourite.asStateFlow()


    fun getMovieFavourite() = viewModelScope.launch {
        _movieFavourite.value = useCase()
    }
}