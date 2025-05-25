package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.errors.Errors

sealed interface Result<out D, out E : Errors> {

    object Initial : Result<Nothing, Nothing>
    object Loading : Result<Nothing, Nothing>
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : Errors>(val error: Errors) : Result<Nothing, E>
}

inline fun <D, E : Errors> Result<D, E>.onLoading(action: () -> Unit): Result<D, E> {
    if (this is Result.Loading) action()
    return this
}

inline fun <D, E : Errors> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(this.data)
    return this
}

inline fun <D, E : Errors> Result<D, E>.onError(action: (Errors) -> Unit): Result<D, E> {
    if (this is Result.Error) action(this.error)
    return this
}