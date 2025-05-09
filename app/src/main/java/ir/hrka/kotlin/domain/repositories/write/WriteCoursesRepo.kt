package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course

interface WriteCoursesRepo {

    suspend fun saveCourses(courses: List<Course>): Resource<Boolean?>
    suspend fun removeCourses(): Resource<Boolean?>
}