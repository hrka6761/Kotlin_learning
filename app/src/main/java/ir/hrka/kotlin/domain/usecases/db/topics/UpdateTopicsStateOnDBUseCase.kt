package ir.hrka.kotlin.domain.usecases.db.topics

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTopicsStateOnDBUseCase @Inject constructor(private val writeTopicsRepo: WriteTopicsRepo) {

    operator fun invoke(
        vararg topicsIds: Int,
        state: Boolean
    ): Flow<Result<Boolean?, BaseError>> =
        flow {
            topicsIds.forEach { topicId ->
                writeTopicsRepo.updateTopicState(topicId, state).collect { updateTopicStateResult ->
                    emit(updateTopicStateResult)
                }
            }
        }
}