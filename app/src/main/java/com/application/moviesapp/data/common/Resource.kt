package com.application.moviesapp.data.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

sealed class Resource<out T: Any> {
    object Loading: Resource<Nothing>()
    data class Success<out T: Any>(val data: T): Resource<T>()
    data class Failure(val throwable: Throwable): Resource<Nothing>()
}