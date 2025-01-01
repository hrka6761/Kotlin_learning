package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.WriteCoursesRepo
import javax.inject.Inject

class SaveDBCoursesUseCase @Inject constructor(private val writeCoursesRepo: WriteCoursesRepo) {

    suspend operator fun invoke(courses: List<Course>) =
        writeCoursesRepo.saveCoursesListOnDB(courses)
}