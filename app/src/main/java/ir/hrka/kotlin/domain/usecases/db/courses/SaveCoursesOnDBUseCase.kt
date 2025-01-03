package ir.hrka.kotlin.domain.usecases.db.courses

import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import javax.inject.Inject

class SaveCoursesOnDBUseCase @Inject constructor(private val writeCoursesRepo: WriteCoursesRepo) {

    suspend operator fun invoke(courses: List<Course>) =
        writeCoursesRepo.saveCoursesListOnDB(courses)
}