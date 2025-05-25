package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.git.VersionsData
import kotlinx.coroutines.flow.Flow

interface ReadChangelogRepo {

    suspend fun getChangeLog(): Flow<Result<VersionsData?, Errors>>
}