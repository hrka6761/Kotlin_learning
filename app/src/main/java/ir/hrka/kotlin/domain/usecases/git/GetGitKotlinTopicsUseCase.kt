package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.core.utilities.Course.Kotlin
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitKotlinTopicsUseCase @Inject constructor(@Named("git") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke() = readTopicsRepo.getTopics(Kotlin)
}