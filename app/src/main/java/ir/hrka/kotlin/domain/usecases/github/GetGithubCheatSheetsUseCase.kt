package ir.hrka.kotlin.domain.usecases.github

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.extractVersionNameFromGradleContent
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.ReadGithubCheatSheetRepo
import javax.inject.Inject

class GetGithubCheatSheetsUseCase @Inject constructor(
    private val readGithubCheatSheetRepo: ReadGithubCheatSheetRepo,
    private val appInfoRepo: AppInfoRepo
) {

    suspend operator fun invoke(): Resource<List<Cheatsheet>?> {
        val versionNameResult = appInfoRepo.getAppInfo()
        val repoFileModelListResult = readGithubCheatSheetRepo.getCheatSheetsList()

        if (versionNameResult is Resource.Error)
            return Resource.Error(versionNameResult.error!!)

        if (repoFileModelListResult is Resource.Error)
            return Resource.Error(repoFileModelListResult.error!!)

        val versionName = versionNameResult
            .data
            ?.content
            ?.decodeBase64()
            ?.extractVersionNameFromGradleContent() ?: ""

        val repoFileModelList = repoFileModelListResult.data
        val sortedRepoFileModelList = repoFileModelList?.sortedBy { item -> item.id }

        val cheatsheetList = sortedRepoFileModelList?.let {
            it.map { repoFileModel ->
                Cheatsheet(
                    id = repoFileModel.id,
                    name = repoFileModel.name,
                    versionName = versionName,
                    hasContentUpdated = true
                )
            }
        }

        return Resource.Success(cheatsheetList)
    }
}