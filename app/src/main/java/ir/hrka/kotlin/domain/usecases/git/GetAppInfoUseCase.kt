package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.string_utilities.extractVersionCodeFromGradleContent
import ir.hrka.kotlin.core.utilities.string_utilities.extractVersionNameFromGradleContent
import ir.hrka.kotlin.core.utilities.string_utilities.extractVersionSuffixFromGradleContent
import ir.hrka.kotlin.domain.entities.AppInfo
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.repositories.git.AppInfoRepo
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(private val appInfoRepo: AppInfoRepo) {

    suspend operator fun invoke(): Resource<AppInfo?> {
        val gradleFile = appInfoRepo.getAppInfo().data
        val encodedGradleContent = gradleFile?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return if (decodedGradleContent.isNotEmpty())
            Resource.Success(provideAppInfoModel(decodedGradleContent))
        else
            Resource.Error(
                Error(
                    READ_FILE_ERROR_CODE,
                    "Can't access the github repository files."
                )
            )
    }


    private fun provideAppInfoModel(gradleText: String): AppInfo {
        val versionCode = gradleText.extractVersionCodeFromGradleContent()
        val versionName = gradleText.extractVersionNameFromGradleContent()
        val versionNameSuffix = gradleText.extractVersionSuffixFromGradleContent()

        return AppInfo(versionCode, versionName, versionNameSuffix)
    }
}