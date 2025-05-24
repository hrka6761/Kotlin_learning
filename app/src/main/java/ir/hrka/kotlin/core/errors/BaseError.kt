package ir.hrka.kotlin.core.errors

interface BaseError {
    val errorCode: Int
    val errorMsg: String
}