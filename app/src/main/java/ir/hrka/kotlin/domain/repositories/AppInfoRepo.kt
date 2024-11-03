package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.RepoFileModel

interface AppInfoRepo {

    suspend fun getAppInfo(): Resource<RepoFileModel?>
}