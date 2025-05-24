package ir.hrka.kotlin.data.repositories.git

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.repositories.read.ReadChangelogRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReadGitChangelogRepoImpl @Inject constructor(private val gitAPI: GitAPI) : ReadChangelogRepo {

    override suspend fun getChangeLog(): Flow<Result<VersionsInfo?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                val response = gitAPI.getChangeLog()

                if (response.isSuccessful) {
                    val changelogFileData = response.body()
                    val changelogFileContent = extractChangelogContent(changelogFileData)

                    if (changelogFileContent.isEmpty()) {
                        emit(
                            Result.Error(
                                Error(
                                    READ_FILE_ERROR_CODE,
                                    "Access to the changelog is not possible."
                                )
                            )
                        )

                        return@flow
                    }

                    val versionsInfo = provideVersionsInfo(changelogFileContent)

                    emit(Result.Success(versionsInfo))
                } else {
                    emit(
                        Result.Error(
                            Error(
                                errorCode = response.code(),
                                errorMsg = response.errorBody()?.string().toString()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        Error(
                            errorCode = RETROFIT_ERROR_CODE,
                            errorMsg = e.message.toString()
                        )
                    )
                )
            }
        }


    private fun extractChangelogContent(changelogFileData: GitFileData?): String {
        val encodedGradleContent = changelogFileData?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return decodedGradleContent
    }

    private fun provideVersionsInfo(changelogContent: String): VersionsInfo =
        Gson().fromJson(changelogContent, VersionsInfo::class.java)
}