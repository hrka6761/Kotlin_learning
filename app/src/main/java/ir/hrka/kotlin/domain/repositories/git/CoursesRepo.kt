package ir.hrka.kotlin.domain.repositories.git

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course

interface CoursesRepo {

    suspend fun getCourses(): Resource<List<Course>?>
}