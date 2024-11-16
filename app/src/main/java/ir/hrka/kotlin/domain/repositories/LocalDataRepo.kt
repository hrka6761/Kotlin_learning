package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource


interface LocalDataRepo {

    suspend fun saveCurrentVersionMinor(minor: Int)
    suspend fun loadCurrentVersionMinor(): Resource<Int>
    suspend fun saveCurrentVersionPatch(minor: Int)
    suspend fun loadCurrentVersionPatch(): Resource<Int>
}