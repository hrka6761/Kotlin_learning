package ir.hrka.kotlin.data.repositories

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.domain.repositories.ChangelogRepo
import javax.inject.Inject

class ChangelogRepoImpl @Inject constructor(private val gitAPI: GitAPI) : ChangelogRepo {

    override suspend fun getChangeLog(): Resource<VersionsInfo?> {
        return try {
            val response = gitAPI.getChangeLog()

            if (response.isSuccessful) {
                val changelogFileData = response.body()
                val changelogFileContent = extractChangelogContent(changelogFileData)
                val versionsInfo = provideVersionsInfo(
                    changelogFileContent.ifEmpty {
                        return Resource.Error(
                            Error(
                                READ_FILE_ERROR_CODE,
                                "Access to the changelog is not possible."
                            )
                        )
                    }
                )

                Resource.Success(versionsInfo)
            } else {
                Resource.Error(
                    Error(
                        errorCode = response.code(),
                        errorMsg = response.errorBody()?.string().toString()
                    )
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
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