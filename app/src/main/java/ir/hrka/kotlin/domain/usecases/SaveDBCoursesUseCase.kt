package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.WriteDBCoursesRepo
import javax.inject.Inject

class SaveDBCoursesUseCase @Inject constructor(private val writeDBCoursesRepo: WriteDBCoursesRepo) {

    suspend operator fun invoke(courses: List<Course>) =
        writeDBCoursesRepo.saveCoursesListOnDB(courses)
}