package ir.hrka.kotlin.domain.usecases.github

import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.extractVersionCodeFromGradleContent
import ir.hrka.kotlin.core.utilities.extractVersionNameFromGradleContent
import ir.hrka.kotlin.core.utilities.extractVersionSuffixFromGradleContent
import ir.hrka.kotlin.domain.entities.AppInfoModel
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.repositories.github.AppInfoRepo
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(private val appInfoRepo: AppInfoRepo) {

    suspend operator fun invoke(): Resource<AppInfoModel?> {
        val gradleFile = appInfoRepo.getAppInfo().data
        val encodedGradleContent = gradleFile?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return if (decodedGradleContent.isNotEmpty())
            Resource.Success(provideAppInfoModel(decodedGradleContent))
        else
            Resource.Error(
                ErrorModel(
                    READ_FILE_ERROR_CODE,
                    "Can't access the github repository files."
                )
            )
    }


    private fun provideAppInfoModel(gradleText: String): AppInfoModel {
        val versionCode = gradleText.extractVersionCodeFromGradleContent()
        val versionName = gradleText.extractVersionNameFromGradleContent()
        val versionNameSuffix = gradleText.extractVersionSuffixFromGradleContent()

        return AppInfoModel(versionCode, versionName, versionNameSuffix)
    }
}