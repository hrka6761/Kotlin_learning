package ir.hrka.kotlin.domain.usecases.db.courses

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import javax.inject.Inject

class UpdateCoursesOnDBUseCase @Inject constructor(private val writeCoursesRepo: WriteCoursesRepo) {

    suspend operator fun invoke(courses: List<Course>): Resource<Boolean?> {
        val removeResult = writeCoursesRepo.removeCourses()

        if (removeResult is Resource.Error)
            return removeResult

        return writeCoursesRepo.saveCourses(courses)
    }
}