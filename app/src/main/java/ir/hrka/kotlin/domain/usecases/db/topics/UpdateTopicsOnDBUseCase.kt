package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTopicsOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    operator fun invoke(topics: List<Topic>, course: Course): Flow<Result<Boolean?, BaseError>> {
        var readyToSave = false

        return flow {
            writeTopicsRepo.removeTopics(course).collect { removeResult ->
                if (removeResult !is Result.Success) emit(removeResult)
                readyToSave = removeResult is Result.Success
            }

            if (readyToSave)
                writeTopicsRepo.saveTopics(topics).collect { saveResult ->
                    if (saveResult !is Result.Loading) emit(saveResult)
                }
        }
    }
}