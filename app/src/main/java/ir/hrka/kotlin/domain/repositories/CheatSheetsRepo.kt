package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.CheatSheetFile

interface CheatSheetsRepo {

    suspend fun getCheatSheets(): Resource<List<CheatSheetFile>?>
}