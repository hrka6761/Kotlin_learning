package ir.hrka.kotlin.domain.usecases.git.kotlin.read

import ir.hrka.kotlin.core.errors.unknownError
import ir.hrka.kotlin.core.utilities.Course.Kotlin
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.string_utilities.extractVersionNameFromGradleContent
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.repositories.git.ChangelogRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import javax.inject.Inject

class GetGitKotlinTopicsListUseCase @Inject constructor(
    private val readGitTopicsRepo: ReadGitTopicsRepo,
    private val changelogRepo: ChangelogRepo
) {

    suspend operator fun invoke(): Resource<List<KotlinTopic>?> {
        val appInfoResult = changelogRepo.getChangeLog()
        val topicsListResult = readGitTopicsRepo.getTopicsList(Kotlin)

        if (appInfoResult is Resource.Error)
            return Resource.Error(appInfoResult.error ?: unknownError)

        if (topicsListResult is Resource.Error)
            return Resource.Error(topicsListResult.error ?: unknownError)

        val versionName = appInfoResult
            .data
            ?.content
            ?.decodeBase64()
            ?.extractVersionNameFromGradleContent() ?: ""

        val gitFilesDataList = topicsListResult.data ?: listOf()
        val sortedGitFilesDataListList = gitFilesDataList.sortedBy { item -> item.id }

        val topicsList = sortedGitFilesDataListList.map { gitFileData ->
            KotlinTopic(
                id = gitFileData.id,
                name = gitFileData.name,
                versionName = versionName,
                hasUpdated = true
            )
        }


        return Resource.Success(topicsList)
    }
}