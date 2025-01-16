package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic

interface ReadTopicsRepo {

    suspend fun getTopics(course: Course): Resource<List<Topic>?>
}