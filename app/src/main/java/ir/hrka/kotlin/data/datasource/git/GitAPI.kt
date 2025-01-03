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
    @GET("courses/course_list.json")
    suspend fun getCourses(): Response<GitFileData>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("courses/{course_directory_name}/{topics_file_name}")
    suspend fun getCourseTopics(
        @Path("course_directory_name") courseName: String,
        @Path("topics_file_name") topicsFileName: String
    ): Response<GitFileData>
}