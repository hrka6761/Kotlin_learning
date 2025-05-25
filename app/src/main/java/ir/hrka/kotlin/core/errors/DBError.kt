package ir.hrka.kotlin.core.errors

data class DBError (
    override val errorCode: Int,
    override val msg: String?,
    val errorDetails: Exception?
) : Errors