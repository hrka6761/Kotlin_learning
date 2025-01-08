package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic

interface WriteTopicsRepo {

    suspend fun saveTopics(topics: List<Topic>): Resource<Boolean?>
    suspend fun removeTopics(course: Course): Resource<Boolean?>
    suspend fun updateTopicState(id: Int, hasContentUpdate: Boolean): Resource<Boolean?>
}