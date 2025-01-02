package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.VersionsInfo

interface ReadChangelogRepo {

    suspend fun getChangeLog(): Resource<VersionsInfo?>
}