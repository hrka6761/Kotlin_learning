package ir.hrka.kotlin.domain.repositories

interface LocalDataRepo {

    suspend fun saveCurrentVersionMinor(minor: Int)
    suspend fun loadCurrentVersionMinor(): Int
    suspend fun saveCurrentVersionPatch(minor: Int)
    suspend fun loadCurrentVersionPatch(): Int
}