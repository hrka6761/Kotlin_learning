package ir.hrka.kotlin.data.repositories.git

import com.google.gson.Gson
import ir.hrka.kotlin.core.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.CoursesData
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReadGitCoursesRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadCoursesRepo {

    override suspend fun getCourses(): Flow<Result<List<Course>?, BaseError>> =
        flow {
            emit(Result.Loading)

            try {
                val response = gitAPI.getCourses()

                if (response.isSuccessful) {
                    val coursesFileData = response.body()
                    val coursesFileContent = extractCoursesContent(coursesFileData)

                    if (coursesFileContent.isEmpty()) {
                        emit(
                            Result.Error(
                                Error(
                                    READ_FILE_ERROR_CODE,
                                    "Access to the courses is not possible."
                                )
                            )
                        )

                        return@flow
                    }

                    val coursesData = provideCourses(coursesFileContent)

                    emit(Result.Success(coursesData.courses))
                } else
                    emit(
                        Result.Error(
                            Error(
                                errorCode = response.code(),
                                errorMsg = response.errorBody()?.string().toString()
                            )
                        )
                    )
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


    private fun extractCoursesContent(coursesFileData: GitFileData?): String {
        val encodedGradleContent = coursesFileData?.content ?: ""
        val decodedGradleContent = encodedGradleContent.decodeBase64()

        return decodedGradleContent
    }

    private fun provideCourses(changelogContent: String): CoursesData =
        Gson().fromJson(changelogContent, CoursesData::class.java)
}