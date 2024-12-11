package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.extractClearPointsFromRawPoint
import ir.hrka.kotlin.core.utilities.extractHeadPointsFromPointContent
import ir.hrka.kotlin.core.utilities.extractJavaDocsFromCheatSheetFileContent
import ir.hrka.kotlin.core.utilities.extractRawPointsFromJavaDocContent
import ir.hrka.kotlin.core.utilities.extractSnippetCodeFromPoint
import ir.hrka.kotlin.core.utilities.extractSubPointsFromPointContent
import ir.hrka.kotlin.data.datasource.github.GitAPI
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import javax.inject.Inject

class ReadGitTopicPointsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadGitTopicPointsRepo {

    override suspend fun getTopicPoints(
        course: Course,
        topicName: String
    ): Resource<List<PointData>?> {
        return try {
            val response =
                gitAPI.getTopicFile(courseName = course.courseName, topicName = topicName)

            if (response.isSuccessful) {
                val cheatSheetFile = response.body()
                val encodedCheatSheetContent = cheatSheetFile?.content ?: ""
                val decodedCheatSheetContent = encodedCheatSheetContent.decodeBase64()

                return if (decodedCheatSheetContent.isNotEmpty())
                    Resource.Success(provideTopicData(decodedCheatSheetContent))
                else
                    Resource.Error(
                        Error(
                            READ_FILE_ERROR_CODE,
                            "Can't access the github repository files."
                        )
                    )
            } else {
                Resource.Error(
                    Error(
                        errorCode = response.code(),
                        errorMsg = response.message()
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


    private fun provideTopicData(decodedKotlinTopicFileContent: String): List<PointData> {
        val content = decodedKotlinTopicFileContent.extractJavaDocsFromCheatSheetFileContent()
        val list = mutableListOf<PointData>()
        var index = 1

        content.forEach { javaDoc ->
            javaDoc.extractRawPointsFromJavaDocContent()
                .forEach { rawPoint ->
                    val clearPoint = rawPoint.extractClearPointsFromRawPoint()
                    val snippets = rawPoint.extractSnippetCodeFromPoint()
                    val headPoint = clearPoint.extractHeadPointsFromPointContent()
                    if (headPoint.isNotEmpty()) {
                        val subPoint = clearPoint.extractSubPointsFromPointContent()
                        val num = index++
                        list.add(
                            PointData(
                                num,
                                -1L,
                                rawPoint,
                                headPoint,
                                subPoint,
                                snippets
                            )
                        )
                    }
                }
        }

        return list
    }
}