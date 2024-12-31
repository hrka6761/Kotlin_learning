package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course

interface WriteDBCoursesRepo {

    suspend fun saveCoursesListOnDB(courses: List<Course>): Resource<Boolean>
    suspend fun clearCoursesListTable(): Resource<Boolean>
}