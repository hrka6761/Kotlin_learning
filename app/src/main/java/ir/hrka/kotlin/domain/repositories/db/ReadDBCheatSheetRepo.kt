package ir.hrka.kotlin.domain.repositories.db

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.entities.db.Cheatsheet

interface ReadDBCheatSheetRepo {

    suspend fun getCheatSheetsList(): Resource<List<Cheatsheet>?>
    suspend fun getCheatSheetPoints(cheatsheetName: String): Resource<List<PointDataModel>?>
}