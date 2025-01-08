package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.repositories.write.WriteSnippetCodesRepo
import javax.inject.Inject

class WriteDBSnippetCodesRepoImpl @Inject constructor(
    private val snippetCodeDao: SnippetCodeDao
) : WriteSnippetCodesRepo {

    override suspend fun saveSnippetCodes(snippetCodes: Array<SnippetCode>): Resource<Boolean?> {
        return try {
            snippetCodeDao.insertSnippetCodes(*snippetCodes)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_WRITE_SNIPPET_CODES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun removeSnippetCodes(pointId: Long): Resource<Boolean?> {
        return try {
            snippetCodeDao.deleteSnippetCodes(pointId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_REMOVE_SNIPPET_CODES_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}