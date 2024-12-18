package ir.hrka.kotlin.domain.repositories.preference

import ir.hrka.kotlin.core.utilities.Resource

interface VersionDataRepo {

    suspend fun saveCurrentKotlinCourseVersionName(versionName: String): Resource<Boolean>
    suspend fun saveCurrentCoroutineCourseVersionName(versionName: String): Resource<Boolean>
    suspend fun loadCurrentKotlinCourseVersionName(): Resource<String>
    suspend fun loadCurrentCoroutineCourseVersionName(): Resource<String>
}