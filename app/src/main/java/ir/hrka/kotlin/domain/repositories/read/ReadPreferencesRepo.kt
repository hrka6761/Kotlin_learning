package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.utilities.Resource

interface ReadPreferencesRepo {

    suspend fun loadCoursesVersionId(): Resource<Int?>
    suspend fun loadKotlinVersionId(): Resource<Int?>
    suspend fun loadCoroutineVersionId(): Resource<Int?>
}