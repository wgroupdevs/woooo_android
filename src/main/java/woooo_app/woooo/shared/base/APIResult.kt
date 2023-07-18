package com.wgroup.woooo_app.woooo.shared.base
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class APIResult<out T> {

    data class Success<out T>(val data: T) : APIResult<T>()
    data class Failure(val msg: Throwable?) : APIResult<Nothing>()
    object Loading : APIResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success $data"
            is Failure -> "Failure $msg"
            Loading -> "Loading"
        }
    }
}

fun <T, R> APIResult<T>.map(transform: (T) -> R): APIResult<R> {
    return when (this) {
        is APIResult.Success -> APIResult.Success(transform(data))
        is APIResult.Failure -> APIResult.Failure(msg)
        APIResult.Loading -> APIResult.Loading
    }
}

fun <T> Flow<APIResult<T>>.doOnSuccess(action: suspend (T) -> Unit): Flow<APIResult<T>> =
    transform { result ->
        if (result is APIResult.Success) {
            action(result.data)
        }
        return@transform emit(result)
    }

fun <T> Flow<APIResult<T>>.doOnFailure(action: suspend (Throwable?) -> Unit): Flow<APIResult<T>> =
    transform { result ->
        if (result is APIResult.Failure) {
            action(result.msg)
        }
        return@transform emit(result)
    }

fun <T> Flow<APIResult<T>>.doOnLoading(action: suspend () -> Unit): Flow<APIResult<T>> =
    transform { result ->
        if (result is APIResult.Loading) {
            action()
        }
        return@transform emit(result)
    }

