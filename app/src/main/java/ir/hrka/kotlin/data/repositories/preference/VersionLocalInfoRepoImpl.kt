package ir.hrka.kotlin.data.repositories.preference

import ir.hrka.kotlin.core.Constants.COROUTINE_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.Constants.LOCAL_DATA_READ_ERROR_CODE
import ir.hrka.kotlin.core.Constants.LOCAL_DATA_WRITE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.KOTLIN_VERSION_ID_PREFERENCE_KEY
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.preference.LocalDataSource
import ir.hrka.kotlin.domain.repositories.preference.VersionLocalInfoRepo
import javax.inject.Inject

class VersionLocalInfoRepoImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : VersionLocalInfoRepo {

    override suspend fun loadKotlinVersionId(): Resource<Int?> {
        return try {
            val versionId = localDataSource.loadInteger(KOTLIN_VERSION_ID_PREFERENCE_KEY, 0)
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
            localDataSource.saveInteger(KOTLIN_VERSION_ID_PREFERENCE_KEY, newVersionId)
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
            val versionId = localDataSource.loadInteger(COROUTINE_VERSION_ID_PREFERENCE_KEY, 0)
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
            localDataSource.saveInteger(COROUTINE_VERSION_ID_PREFERENCE_KEY, newVersionId)
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