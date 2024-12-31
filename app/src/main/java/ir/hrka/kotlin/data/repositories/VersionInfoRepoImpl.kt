package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.COROUTINE_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.COURSES_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_ID
import ir.hrka.kotlin.core.Constants.LOCAL_DATA_READ_ERROR_CODE
import ir.hrka.kotlin.core.Constants.LOCAL_DATA_WRITE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.KOTLIN_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.preference.PreferenceDataSource
import ir.hrka.kotlin.domain.repositories.VersionInfoRepo
import javax.inject.Inject

class VersionInfoRepoImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) : VersionInfoRepo {

    override suspend fun loadCoursesVersionId(): Resource<Int?> {
        return try {
            val versionId =
                preferenceDataSource.loadInteger(COURSES_VERSION_ID_PREFERENCE_KEY, DEFAULT_VERSION_ID)
            Resource.Success(versionId)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_WRITE_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }

    override suspend fun saveCoursesVersionId(newVersionId: Int): Resource<Boolean?> {
        return try {
            preferenceDataSource.saveInteger(COURSES_VERSION_ID_PREFERENCE_KEY, newVersionId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_READ_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }

    override suspend fun loadKotlinVersionId(): Resource<Int?> {
        return try {
            val versionId =
                preferenceDataSource.loadInteger(KOTLIN_VERSION_ID_PREFERENCE_KEY, DEFAULT_VERSION_ID)
            Resource.Success(versionId)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_WRITE_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }

    override suspend fun saveKotlinVersionId(newVersionId: Int): Resource<Boolean?> {
        return try {
            preferenceDataSource.saveInteger(KOTLIN_VERSION_ID_PREFERENCE_KEY, newVersionId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_READ_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }

    override suspend fun loadCoroutineVersionId(): Resource<Int?> {
        return try {
            val versionId =
                preferenceDataSource.loadInteger(COROUTINE_VERSION_ID_PREFERENCE_KEY, DEFAULT_VERSION_ID)
            Resource.Success(versionId)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_WRITE_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }

    override suspend fun saveCoroutineVersionId(newVersionId: Int): Resource<Boolean?> {
        return try {
            preferenceDataSource.saveInteger(COROUTINE_VERSION_ID_PREFERENCE_KEY, newVersionId)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_READ_ERROR_CODE,
                    errorMsg = "Can't read current version id."
                )
            )
        }
    }
}