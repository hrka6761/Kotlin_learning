package ir.hrka.kotlin.core.errors

data class Error(
    override val errorCode: Int,
    override val errorMsg: String
) : BaseError