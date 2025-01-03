package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.core.utilities.Course.Coroutine
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitCoroutineTopicsUseCase @Inject constructor(@Named("git") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke() = readTopicsRepo.getTopics(Coroutine)
}