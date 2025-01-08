package ir.hrka.kotlin.domain.usecases.git

import ir.hrka.kotlin.domain.repositories.read.ReadChangelogRepo
import javax.inject.Inject

class GetChangelogFromGitUseCase @Inject constructor(private val readChangelogRepo: ReadChangelogRepo) {

    suspend operator fun invoke() = readChangelogRepo.getChangeLog()
}