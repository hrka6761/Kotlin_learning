package ir.hrka.kotlin.core.errors

data class RemoteError(
    override val errorCode: Int,
    override val msg: String?,
    val errorBodyMsg: String
) : Errors