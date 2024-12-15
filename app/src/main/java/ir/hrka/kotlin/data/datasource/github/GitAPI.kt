package ir.hrka.kotlin.data.datasource.github

import ir.hrka.kotlin.domain.entities.GitFileData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GitAPI {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/build.gradle.kts")
    suspend fun getAppInfo(): Response<GitFileData>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/src/main/java/ir/hrka/kotlin/{courseName}")
    suspend fun getTopicsList(@Path("courseName") courseName: String): Response<List<GitFileData>>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/src/main/java/ir/hrka/kotlin/{courseName}/{topicName}")
    suspend fun getTopicFile(
        @Path("courseName") courseName: String,
        @Path("topicName") topicName: String
    ): Response<GitFileData>
}