package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.errors.BaseError

sealed interface Result<out D, out E : BaseError> {

    object Initial : Result<Nothing, Nothing>
    object Loading : Result<Nothing, Nothing>
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : BaseError>(val error: BaseError) : Result<Nothing, E>
}

inline fun <D, E : BaseError> Result<D, E>.onLoading(action: () -> Unit): Result<D, E> {
    if (this is Result.Loading) action()
    return this
}

inline fun <D, E : BaseError> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(this.data)
    return this
}

inline fun <D, E : BaseError> Result<D, E>.onError(action: (BaseError) -> Unit): Result<D, E> {
    if (this is Result.Error) action(this.error)
    return this
}