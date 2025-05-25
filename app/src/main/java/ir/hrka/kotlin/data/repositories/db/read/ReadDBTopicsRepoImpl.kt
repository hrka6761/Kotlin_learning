package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadDBTopicsRepoImpl @Inject constructor(
    private val topicDao: TopicDao,
) : ReadTopicsRepo {

    override suspend fun getTopics(course: Course): Flow<Result<List<Topic>?, Errors>> =
        handleDBResponse { topicDao.getTopics(course.courseName) }
}