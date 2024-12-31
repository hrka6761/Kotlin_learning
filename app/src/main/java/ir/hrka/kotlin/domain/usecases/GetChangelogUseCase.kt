package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.ChangelogRepo
import javax.inject.Inject

class GetChangelogUseCase @Inject constructor(private val changelogRepo: ChangelogRepo) {

    suspend operator fun invoke() = changelogRepo.getChangeLog()
}