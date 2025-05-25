package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleRemoteResponse
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.git.TopicsData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadGitTopicsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadTopicsRepo {

    override suspend fun getTopics(course: Course): Flow<Result<List<Topic>?, Errors>> =
        handleRemoteResponse(TopicsData::class.java) {
            gitAPI.getCourseTopics(course.courseName, course.topicsFileName)
        }
}