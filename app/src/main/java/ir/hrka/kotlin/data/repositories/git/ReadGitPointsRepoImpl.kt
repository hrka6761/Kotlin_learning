package ir.hrka.kotlin.data.repositories.git

import android.util.Log
import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import javax.inject.Inject

class ReadGitPointsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadPointsRepo {

    override suspend fun getPoints(topic: Topic): Resource<List<Point>?> {
        return try {
            val response = gitAPI.getTopicPoints(topic.courseName, topic.fileName)

            if (response.isSuccessful) {
                val topicFileData = response.body()
                val topicFileContent = extractTopicPointsContent(topicFileData)
                val pointsData = providePointData(
                    topicFileContent.ifEmpty {
                        return Resource.Error(
                            Error(
                                READ_FILE_ERROR_CODE,
                                "Can't read Points from the github."
                            )
                        )
                    }
                )
                Resource.Success(pointsData.points)
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


    private fun extractTopicPointsContent(topicFileData: GitFileData?): String {
        val encodedContent = topicFileData?.content ?: ""
        val decodedContent = encodedContent.decodeBase64()

        return decodedContent
    }

    private fun providePointData(topicFileContent: String): PointData =
        Gson().fromJson(topicFileContent, PointData::class.java)
}