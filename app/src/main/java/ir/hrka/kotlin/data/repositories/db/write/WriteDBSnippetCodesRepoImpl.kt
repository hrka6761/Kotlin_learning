package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.repositories.write.WriteSnippetCodesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteDBSnippetCodesRepoImpl @Inject constructor(
    private val snippetCodeDao: SnippetCodeDao
) : WriteSnippetCodesRepo {

    override suspend fun saveSnippetCodes(
        snippetCodes: Array<SnippetCode>
    ): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            snippetCodeDao.insertSnippetCodes(*snippetCodes)
            return@handleDBResponse true
        }


    override suspend fun removeSnippetCodes(pointId: Long): Flow<Result<Boolean?, Errors>> =
        handleDBResponse {
            snippetCodeDao.deleteSnippetCodes(pointId)
            return@handleDBResponse true
        }
}