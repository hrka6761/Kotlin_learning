package ir.hrka.kotlin.data.repositories

import android.util.Log
import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.KotlinTopicsData
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.ReadTopicsRepo
import javax.inject.Inject

class GitReadTopicsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadTopicsRepo {

    override suspend fun getKotlinTopics(): Resource<List<Topic>?> {
        return try {
            val response = gitAPI.getKotlinTopics()

            if (response.isSuccessful) {
                val kotlinTopicFileData = response.body()
                val kotlinTopicFileContent = extractKotlinTopicContent(kotlinTopicFileData)
                val kotlinTopic = provideKotlinTopic(
                    kotlinTopicFileContent.ifEmpty {
                        return Resource.Error(
                            Error(
                                READ_FILE_ERROR_CODE,
                                "Access to the kotlin topics is not possible."
                            )
                        )
                    }
                )
                kotlinTopic.topics.forEach { it.hasUpdate = true }
                Resource.Success(kotlinTopic.topics)
            } else {
                Resource.Error(
                    Error(
                        errorCode = response.code(),
                        errorMsg = response.errorBody()?.string().toString()
                    )
                )
            }
        } catch (e: Exception) {
            Log.i(TAG, "getKotlinTopics: ${e.message}")
            Resource.Error(
                Error(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }

    override suspend fun getCoroutineTopics(): Resource<List<Topic>?> {
        TODO("Not yet implemented")
    }


    private fun extractKotlinTopicContent(kotlinTopicFileData: GitFileData?): String {
        val encodedContent = kotlinTopicFileData?.content ?: ""
        val decodedContent = encodedContent.decodeBase64()

        return decodedContent
    }

    private fun provideKotlinTopic(kotlinTopicFileData: String): KotlinTopicsData =
        Gson().fromJson(kotlinTopicFileData, KotlinTopicsData::class.java)
}