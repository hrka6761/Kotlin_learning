package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.repositories.git.ChangelogRepo
import javax.inject.Inject

class ChangelogRepoImpl @Inject constructor(private val gitAPI: GitAPI) : ChangelogRepo {

    override suspend fun getChangeLog(): Resource<GitFileData?> {
        return try {
            val response = gitAPI.getChangeLog()

            if (response.isSuccessful)
                Resource.Success(response.body())
            else
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
}