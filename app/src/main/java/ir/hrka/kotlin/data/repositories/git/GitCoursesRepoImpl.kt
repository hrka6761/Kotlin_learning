package ir.hrka.kotlin.data.repositories.git

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.string_utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.CoursesData
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.git.CoursesRepo
import javax.inject.Inject

class GitCoursesRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : CoursesRepo {

    override suspend fun getCourses(): Resource<List<Course>?> {
        return try {
            val response = gitAPI.getCourses()

            if (response.isSuccessful) {
                val coursesFileData = response.body()
                val coursesFileContent = extractCoursesContent(coursesFileData)
                val coursesData = provideCourses(
                    coursesFileContent.ifEmpty {
                        return Resource.Error(
                            Error(
                                READ_FILE_ERROR_CODE,
                                "Access to the changelog is not possible."
                            )
                        )
                    }
                )

                Resource.Success(coursesData.courses)
            } else
                Resource.Error(
                    Error(
                        errorCode = response.code(),
                        errorMsg = response.errorBody()?.string().toString()
                    )
                )
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }


    private fun extractCoursesContent(coursesFileData: GitFileData?): String {
        val encodedGradleContent = coursesFileData?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return decodedGradleContent
    }

    private fun provideCourses(changelogContent: String): CoursesData =
        Gson().fromJson(changelogContent, CoursesData::class.java)
}