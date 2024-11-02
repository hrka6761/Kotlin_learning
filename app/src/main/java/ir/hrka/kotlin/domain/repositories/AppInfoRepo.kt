package ir.hrka.kotlin.domain.repositories

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.CheatSheetFile

interface AppInfoRepo {

    suspend fun getAppInfo(): Resource<CheatSheetFile?>
}