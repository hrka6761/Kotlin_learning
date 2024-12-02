package ir.hrka.kotlin.domain.repositories.preference

import ir.hrka.kotlin.core.utilities.Resource

interface VersionDataRepo {

    suspend fun saveCurrentVersionName(versionName: String): Resource<Boolean>
    suspend fun loadCurrentVersionName(): Resource<String>
}