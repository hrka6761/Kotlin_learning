package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.utilities.Constants.MINOR_VERSION_KEY
import ir.hrka.kotlin.core.utilities.Constants.READ_MINOR_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.LocalDataSource
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.repositories.LocalDataRepo
import javax.inject.Inject

class LocalDataRepoImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalDataRepo {

    override suspend fun saveCurrentVersionMinor(minor: Int) {
        localDataSource.saveInteger(MINOR_VERSION_KEY, minor)
    }

    override suspend fun loadCurrentVersionMinor(): Resource<Int> {
        val minor = localDataSource.loadInteger(MINOR_VERSION_KEY, -1)

        return if (minor < 0)
            Resource.Success(minor)
        else
            Resource.Error(
                ErrorModel(
                    errorCode = READ_MINOR_ERROR_CODE,
                    errorMsg = "Cant access current version."
                )
            )
    }

    override suspend fun saveCurrentVersionPatch(minor: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun loadCurrentVersionPatch(): Resource<Int> {
        TODO("Not yet implemented")
    }
}