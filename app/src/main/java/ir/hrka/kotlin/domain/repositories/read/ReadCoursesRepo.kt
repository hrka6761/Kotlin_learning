package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course

interface ReadCoursesRepo {

    suspend fun getCourses(): Resource<List<Course>?>
}