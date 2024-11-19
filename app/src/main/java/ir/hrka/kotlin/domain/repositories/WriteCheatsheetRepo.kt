package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint

interface WriteCheatsheetRepo {

    suspend fun saveCheatSheetsOnDB(cheatsheets: List<Cheatsheet>): Resource<Boolean>
    suspend fun clearCheatsheetTable(): Resource<Boolean>
    suspend fun updateCheatsheetStateOnDB(id: Int, hasContentUpdated: Boolean): Resource<Boolean>
    suspend fun savePointsOnDB(point: Point): Resource<Long>
    suspend fun saveSubPointsOnDB(subPoints: Array<SubPoint>): Resource<Boolean>
    suspend fun saveSnippetCodesOnDB(snippetCodes: Array<SnippetCode>): Resource<Boolean>
    suspend fun deleteCheatsheetPoints(cheatsheetName: String): Resource<Boolean>
}