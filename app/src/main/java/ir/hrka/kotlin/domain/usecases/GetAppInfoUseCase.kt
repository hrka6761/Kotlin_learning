package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import javax.inject.Inject

class GetAppInfoUseCase @Inject constructor(private val appInfoRepo: AppInfoRepo) {

    suspend operator fun invoke() = appInfoRepo.getAppInfo()
}