package ir.hrka.kotlin.core.utilities

sealed class Resource<T>(
    val data: T? = null,
    val error: ir.hrka.kotlin.core.errors.Error? = null
) {

    class Initial<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(error: ir.hrka.kotlin.core.errors.Error) : Resource<T>(error = error)
}