package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import javax.inject.Inject

class ReadGitTopicsRepoImpl @Inject constructor(private val gitAPI: GitAPI) : ReadGitTopicsRepo {

    override suspend fun getTopicsList(course: Course): Resource<List<GitFileData>?> {
        return try {
            val response = gitAPI.getTopicsList(courseName = course.courseName)

            if (response.isSuccessful)
                Resource.Success(response.body())
            else
                Resource.Error(
                    Error(
                        errorCode = response.code(),
                        errorMsg = response.message()
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
}