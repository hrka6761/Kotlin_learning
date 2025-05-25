package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleRemoteResponse
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.git.VersionsData
import ir.hrka.kotlin.domain.repositories.read.ReadChangelogRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadGitChangelogRepoImpl @Inject constructor(private val gitAPI: GitAPI) : ReadChangelogRepo {

    override suspend fun getChangeLog(): Flow<Result<VersionsData?, Errors>> =
        handleRemoteResponse(VersionsData::class.java) { gitAPI.getChangeLog() }
}