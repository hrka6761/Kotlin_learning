package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo

interface ChangelogRepo {

    suspend fun getChangeLog(): Resource<VersionsInfo?>
}