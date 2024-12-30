package ir.hrka.kotlin.domain.repositories.preference

import ir.hrka.kotlin.core.utilities.Resource

interface LocalVersionInfoRepo {

    suspend fun loadCoursesVersionId(): Resource<Int?>
    suspend fun saveCoursesVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun loadKotlinVersionId(): Resource<Int?>
    suspend fun saveKotlinVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun loadCoroutineVersionId(): Resource<Int?>
    suspend fun saveCoroutineVersionId(newVersionId: Int): Resource<Boolean?>
}