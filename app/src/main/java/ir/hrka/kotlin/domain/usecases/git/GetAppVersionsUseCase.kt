package ir.hrka.kotlin.domain.usecases.git

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.domain.entities.VersionsInfo
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.repositories.git.ChangelogRepo
import javax.inject.Inject

class GetAppVersionsUseCase @Inject constructor(private val changelogRepo: ChangelogRepo) {

    suspend operator fun invoke(): Resource<VersionsInfo> {
        val changelogFileDataResult = changelogRepo.getChangeLog()

        if (changelogFileDataResult is Resource.Error)
            return Resource.Error(changelogFileDataResult.error!!)

        val changelogFileData = changelogFileDataResult.data
        val changelogContent = extractChangelogContent(changelogFileData)

        return if (changelogContent.isNotEmpty())
            Resource.Success(provideVersionsInfo(changelogContent))
        else
            Resource.Error(
                Error(
                    READ_FILE_ERROR_CODE,
                    "Access to the changelog is not possible."
                )
            )
    }


    private fun extractChangelogContent(changelogFileData: GitFileData?): String {
        val encodedGradleContent = changelogFileData?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return decodedGradleContent
    }

    private fun provideVersionsInfo(changelogContent: String): VersionsInfo =
        Gson().fromJson(changelogContent, VersionsInfo::class.java)
}