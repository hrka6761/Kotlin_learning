package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource

interface VersionInfoRepo {

    suspend fun loadCoursesVersionId(): Resource<Int?>
    suspend fun saveCoursesVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun loadKotlinVersionId(): Resource<Int?>
    suspend fun saveKotlinVersionId(newVersionId: Int): Resource<Boolean?>
    suspend fun loadCoroutineVersionId(): Resource<Int?>
    suspend fun saveCoroutineVersionId(newVersionId: Int): Resource<Boolean?>
}