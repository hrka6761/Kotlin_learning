package ir.hrka.kotlin.core.error

interface Error {

    val msg: String

    operator fun invoke()
}