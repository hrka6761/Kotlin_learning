package ir.hrka.kotlin.data.datasource.git

import ir.hrka.kotlin.domain.entities.GitFileData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface GitAPI {

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("changelog/changelog.json")
    suspend fun getChangeLog(): Response<GitFileData>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("courses/course_list.json")
    suspend fun getCourses(): Response<GitFileData>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("courses/kotlin/kotlin_topics_list.json")
    suspend fun getKotlinTopics(): Response<GitFileData>
}