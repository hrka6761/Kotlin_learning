package ir.hrka.kotlin.domain.usecases.git.coroutine.read

import android.util.Log
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.UNKNOWN_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Course.Coroutine
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.extractVersionNameFromGradleContent
import ir.hrka.kotlin.core.utilities.isTopicVisualized
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.repositories.git.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import javax.inject.Inject

class GetGitCoroutineTopicsListUseCase @Inject constructor(
    private val readGitTopicsRepo: ReadGitTopicsRepo,
    private val appInfoRepo: AppInfoRepo
) {

    private val unknownError = Error(errorCode = UNKNOWN_ERROR_CODE, errorMsg = "unknown error !!!")

    suspend operator fun invoke(): Resource<List<CoroutineTopic>?> {
        val appInfoResult = appInfoRepo.getAppInfo()
        val topicsListResult = readGitTopicsRepo.getTopicsList(Coroutine)

        Log.i(TAG, "appInfoResult: $appInfoResult")
        Log.i(TAG, "topicsListResult: $topicsListResult")

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