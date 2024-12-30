package ir.hrka.kotlin.domain.usecases.db.cources

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoursesRepo
import javax.inject.Inject

class PutDBCoursesUseCase @Inject constructor(private val writeDBCoursesRepo: WriteDBCoursesRepo) {

    suspend operator fun invoke(courses: List<Course>) =
        writeDBCoursesRepo.saveCoursesListOnDB(courses)
}