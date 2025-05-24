package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.VersionsInfo
import kotlinx.coroutines.flow.Flow

interface ReadChangelogRepo {

    suspend fun getChangeLog(): Flow<Result<VersionsInfo?, BaseError>>
}