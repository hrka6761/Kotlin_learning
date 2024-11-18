package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Cheatsheet

interface DBRepo {

    suspend fun saveCheatSheetsOnDB(cheatsheets: List<Cheatsheet>): Resource<Boolean>
    suspend fun clearDB(): Resource<Boolean>
    suspend fun updateCheatsheetUpdateStateOnDB(
        id: Int,
        hasContentUpdated: Boolean
    ): Resource<Boolean>
}