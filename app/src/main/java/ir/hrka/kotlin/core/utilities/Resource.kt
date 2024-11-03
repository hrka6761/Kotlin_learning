package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.domain.entities.ErrorModel

sealed class Resource<T>(
    val data: T? = null,
    val error: ErrorModel? = null
) {

    class Initial<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(error: ErrorModel) : Resource<T>(error = error)
}