package ir.hrka.kotlin.data.repositories.git

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.TopicsData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject

class ReadGitTopicsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadTopicsRepo {

    override suspend fun getTopics(course: Course): Resource<List<Topic>?> {
        return try {
            val response = gitAPI.getCourseTopics(
                courseName = course.courseName,
                topicsListFileName = course.topicsFileName
            )

            if (response.isSuccessful) {
                val topicFileData = response.body()
                val topicFileContent = extractTopicsListContent(topicFileData)
                val topicsData = provideTopicData(
                    topicFileContent.ifEmpty {
                        return Resource.Error(
                            Error(
                                READ_FILE_ERROR_CODE,
                                "Can't read topics from the github."
                            )
                        )
                    }
                )

                Resource.Success(topicsData.topics)
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


    private fun extractTopicsListContent(kotlinTopicFileData: GitFileData?): String {
        val encodedContent = kotlinTopicFileData?.content ?: ""
        val decodedContent = encodedContent.decodeBase64()

        return decodedContent
    }

    private fun provideTopicData(topicFileData: String): TopicsData =
        Gson().fromJson(topicFileData, TopicsData::class.java)
}