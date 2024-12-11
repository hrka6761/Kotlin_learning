package ir.hrka.kotlin.domain.repositories.git

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.GitFileData

interface AppInfoRepo {

    suspend fun getAppInfo(): Resource<GitFileData?>
}