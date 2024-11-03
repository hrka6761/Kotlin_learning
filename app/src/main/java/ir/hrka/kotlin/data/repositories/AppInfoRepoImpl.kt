package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.utilities.Constants.RETROFIT_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Constants.TOKEN
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.GithubAPI
import ir.hrka.kotlin.domain.entities.CheatSheetFile
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import javax.inject.Inject

class AppInfoRepoImpl @Inject constructor(
    private val githubAPI: GithubAPI
) : AppInfoRepo {

    override suspend fun getAppInfo(): Resource<CheatSheetFile?> {
        return try {
            val response = githubAPI.getAppInfo(TOKEN)
            if (response.isSuccessful)
                Resource.Success(response.body())
            else
                Resource.Error(ErrorModel(errorCode = response.code(), errorMsg = response.errorBody()?.string().toString()))
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = RETROFIT_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}