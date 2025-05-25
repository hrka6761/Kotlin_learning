package ir.hrka.kotlin.core.utilities

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ir.hrka.kotlin.core.Constants.DB_ERROR_CODE
import ir.hrka.kotlin.core.Constants.EXTRACTION_ERROR_CODE
import ir.hrka.kotlin.core.Constants.JSON_CONVERSION_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.DBError
import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.errors.JsonConversionError
import ir.hrka.kotlin.core.errors.RemoteError
import ir.hrka.kotlin.core.errors.RetrofitError
import ir.hrka.kotlin.domain.entities.git.Data
import ir.hrka.kotlin.domain.entities.git.GitFileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


inline fun <reified D, E : Errors> handleDBResponse(
    crossinline dbSourceCall: suspend () -> D
): Flow<Result<D, E>> =
    flow {
        emit(Result.Loading)

        try {
            val data = dbSourceCall()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(
                Result.Error(
                    DBError(
                        errorCode = DB_ERROR_CODE,
                        msg = e.message,
                        errorDetails = e
                    )
                )
            )
        }
    }


inline fun <reified D, E : Errors, T : Data<*>> handleRemoteResponse(
    dataModelClass: Class<T>,
    crossinline remoteSourceCall: suspend () -> Response<GitFileData>
): Flow<Result<D, E>> =
    flow {
        emit(Result.Loading)

        try {
            val response = remoteSourceCall()

            if (response.isSuccessful) {
                val fileData = response.body()
                val jsonContent = extractContent(fileData)

                if (jsonContent.isEmpty()) {
                    emit(
                        Result.Error(
                            RemoteError(
                                errorCode = EXTRACTION_ERROR_CODE,
                                msg = response.message(),
                                errorBodyMsg = response.errorBody()?.string().toString()
                            )
                        )
                    )
                    return@flow
                }

                val model = Gson().fromJson<T>(jsonContent, dataModelClass)
                emit(Result.Success(model.getMasterData() as D))
            } else
                emit(
                    Result.Error(
                        RemoteError(
                            errorCode = response.code(),
                            msg = response.message(),
                            errorBodyMsg = response.errorBody()?.string().toString()
                        )
                    )
                )

        } catch (e: JsonSyntaxException) {
            emit(
                Result.Error(
                    JsonConversionError(
                        errorCode = JSON_CONVERSION_ERROR_CODE,
                        msg = e.message,
                        errorDetails = e
                    )
                )
            )
        } catch (e: Exception) {
            emit(
                Result.Error(
                    RetrofitError(
                        errorCode = RETROFIT_ERROR_CODE,
                        msg = e.message,
                        errorDetails = e
                    )
                )
            )
        }
    }


fun extractContent(fileData: GitFileData?): String {
    val encodedGradleContent = fileData?.content ?: ""
    val decodedGradleContent = encodedGradleContent.decodeBase64()

    return decodedGradleContent
}