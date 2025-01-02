package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource

interface WritePreferencesRepo {

    suspend fun saveCoursesVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun saveKotlinVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun saveCoroutineVersionId(newVersionId: Int): Resource<Boolean?>
}