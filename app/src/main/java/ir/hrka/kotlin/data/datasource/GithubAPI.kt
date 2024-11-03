package ir.hrka.kotlin.data.datasource

import ir.hrka.kotlin.domain.entities.CheatSheetFile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GithubAPI {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/build.gradle.kts")
    suspend fun getAppInfo(@Header("Authorization") token: String): Response<CheatSheetFile>
}