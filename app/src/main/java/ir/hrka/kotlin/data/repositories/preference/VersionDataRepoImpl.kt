package ir.hrka.kotlin.data.repositories.preference

import ir.hrka.kotlin.core.Constants.LOCAL_DATA_READ_ERROR_CODE
import ir.hrka.kotlin.core.Constants.LOCAL_DATA_WRITE_ERROR_CODE
import ir.hrka.kotlin.core.Constants.VERSION_NAME_KEY
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.preference.LocalDataSource
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import javax.inject.Inject

class VersionDataRepoImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : VersionDataRepo {

    override suspend fun saveCurrentVersionName(versionName: String): Resource<Boolean> {
        return try {
            localDataSource.saveString(VERSION_NAME_KEY, versionName)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_READ_ERROR_CODE,
                    errorMsg = "Can't read current version minor."
                )
            )
        }
    }

    override suspend fun loadCurrentVersionName(): Resource<String> {
        return try {
            val minor = localDataSource.loadString(VERSION_NAME_KEY, "")
            Resource.Success(minor)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = LOCAL_DATA_WRITE_ERROR_CODE,
                    errorMsg = "Can't read current version minor."
                )
            )
        }
    }
}