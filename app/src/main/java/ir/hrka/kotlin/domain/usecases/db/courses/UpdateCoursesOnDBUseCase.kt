package ir.hrka.kotlin.domain.usecases.db.courses

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCoursesOnDBUseCase @Inject constructor(private val writeCoursesRepo: WriteCoursesRepo) {

    operator fun invoke(courses: List<Course>): Flow<Result<Boolean?, Errors>> {
        var readyToSave = false

        return flow {
            writeCoursesRepo.removeCourses().collect { removeResult ->
                if (removeResult !is Result.Success) emit(removeResult)
                readyToSave = removeResult is Result.Success
            }

            if (readyToSave)
                writeCoursesRepo.saveCourses(courses).collect { saveResult ->
                    if (saveResult !is Result.Loading) emit(saveResult)
                }
        }
    }
}