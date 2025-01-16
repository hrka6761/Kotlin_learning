package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_COURSES_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject

class ReadDBTopicsRepoImpl @Inject constructor(
    private val topicDao: TopicDao,
) : ReadTopicsRepo {

    override suspend fun getTopics(course: Course): Resource<List<Topic>?> {
        return try {
            Resource.Success(topicDao.getTopics(course.courseName))
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_COURSES_ERROR_CODE,
                    errorMsg = "Can't read topics from the database."
                )
            )
        }
    }
}