package ir.hrka.kotlin.data.repositories.db.write

import ir.hrka.kotlin.core.Constants.DB_REMOVE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_WRITE_SNIPPET_CODES_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.repositories.write.WriteSnippetCodesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WriteDBSnippetCodesRepoImpl @Inject constructor(
    private val snippetCodeDao: SnippetCodeDao
) : WriteSnippetCodesRepo {

    override suspend fun saveSnippetCodes(
        snippetCodes: Array<SnippetCode>
    ): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                snippetCodeDao.insertSnippetCodes(*snippetCodes)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_WRITE_SNIPPET_CODES_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    override suspend fun removeSnippetCodes(pointId: Long): Flow<Result<Boolean?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                snippetCodeDao.deleteSnippetCodes(pointId)
                emit(Result.Success(true))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = DB_REMOVE_SNIPPET_CODES_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }
}