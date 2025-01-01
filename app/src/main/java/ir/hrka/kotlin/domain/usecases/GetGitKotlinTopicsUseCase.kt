package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitKotlinTopicsUseCase @Inject constructor(@Named("git") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke() = readTopicsRepo.getKotlinTopics()
}