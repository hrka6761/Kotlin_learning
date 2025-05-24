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
import ir.hrka.kotlin.domain.entities.TopicsData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReadGitTopicsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadTopicsRepo {

    override suspend fun getTopics(course: Course): Flow<Result<List<Topic>?, BaseError>> =
        flow {
            try {
                val response = gitAPI.getCourseTopics(
                    courseName = course.courseName,
                    topicsListFileName = course.topicsFileName
                )

                if (response.isSuccessful) {
                    val topicFileData = response.body()
                    val topicFileContent = extractTopicsListContent(topicFileData)

                    if (topicFileContent.isEmpty()) {
                        emit(
                            Result.Error(
                                Error(
                                    READ_FILE_ERROR_CODE,
                                    "Can't read topics from the github."
                                )
                            )
                        )

                        return@flow
                    }

                    val topicsData = provideTopicData(topicFileContent)

                    emit(Result.Success(topicsData.topics))
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



    private fun extractTopicsListContent(kotlinTopicFileData: GitFileData?): String {
        val encodedContent = kotlinTopicFileData?.content ?: ""
        val decodedContent = encodedContent.decodeBase64()

        return decodedContent
    }

    private fun provideTopicData(topicFileData: String): TopicsData =
        Gson().fromJson(topicFileData, TopicsData::class.java)
}