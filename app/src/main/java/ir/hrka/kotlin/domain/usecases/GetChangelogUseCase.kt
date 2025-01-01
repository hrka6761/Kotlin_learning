package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ReadChangelogRepo
import javax.inject.Inject

class GetChangelogUseCase @Inject constructor(private val readChangelogRepo: ReadChangelogRepo) {

    suspend operator fun invoke() = readChangelogRepo.getChangeLog()
}