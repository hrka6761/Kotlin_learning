package ir.hrka.kotlin.data.datasource.git

import ir.hrka.kotlin.domain.entities.GitFileData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GitAPI {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("changelog/changelog.json")
    suspend fun getChangeLog(): Response<GitFileData>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/src/main/java/ir/hrka/kotlin/courses/{courseName}")
    suspend fun getTopicsList(@Path("courseName") courseName: String): Response<List<GitFileData>>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("app/src/main/java/ir/hrka/kotlin/courses/{courseName}/{topicName}")
    suspend fun getTopicFile(
        @Path("courseName") courseName: String,
        @Path("topicName") topicName: String
    ): Response<GitFileData>
}