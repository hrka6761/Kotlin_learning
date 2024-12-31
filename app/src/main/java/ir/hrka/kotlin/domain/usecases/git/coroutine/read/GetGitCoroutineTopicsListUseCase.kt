package ir.hrka.kotlin.domain.usecases.git.coroutine.read

import ir.hrka.kotlin.core.errors.unknownError
import ir.hrka.kotlin.core.utilities.Course.Coroutine
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.isTopicVisualized
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.ChangelogRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import javax.inject.Inject

class GetGitCoroutineTopicsListUseCase @Inject constructor(
    private val readGitTopicsRepo: ReadGitTopicsRepo,
    private val changelogRepo: ChangelogRepo
) {

    suspend operator fun invoke(): Resource<List<CoroutineTopic>?> {
        val appInfoResult = changelogRepo.getChangeLog()
        val topicsListResult = readGitTopicsRepo.getTopicsList(Coroutine)

        if (appInfoResult is Resource.Error)
            return Resource.Error(appInfoResult.error ?: unknownError)

        if (topicsListResult is Resource.Error)
            return Resource.Error(topicsListResult.error ?: unknownError)

        val versionName =  ""

        val gitFilesDataList = topicsListResult.data ?: listOf()
        val sortedGitFilesDataListList = gitFilesDataList.sortedBy { item -> item.id }

        val topicsList = sortedGitFilesDataListList.map { gitFileData ->
            CoroutineTopic(
                id = gitFileData.id,
                name = gitFileData.name,
                versionName = versionName,
                hasVisualizer = gitFileData.name.isTopicVisualized(),
                hasUpdated = true
            )
        }


        return Resource.Success(topicsList)
    }
}