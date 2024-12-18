package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.GitFileData
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.repositories.git.AppInfoRepo
import javax.inject.Inject

class AppInfoRepoImpl @Inject constructor(private val gitAPI: GitAPI) : AppInfoRepo {

    override suspend fun getAppInfo(): Resource<GitFileData?> {
        return try {
            val response = gitAPI.getAppInfo()

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