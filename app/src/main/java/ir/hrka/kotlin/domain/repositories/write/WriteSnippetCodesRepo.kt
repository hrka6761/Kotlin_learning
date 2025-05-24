package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import kotlinx.coroutines.flow.Flow

interface WriteSnippetCodesRepo {

    suspend fun saveSnippetCodes(snippetCodes: Array<SnippetCode>): Flow<Result<Boolean?, BaseError>>
    suspend fun removeSnippetCodes(pointId: Long): Flow<Result<Boolean?, BaseError>>
}