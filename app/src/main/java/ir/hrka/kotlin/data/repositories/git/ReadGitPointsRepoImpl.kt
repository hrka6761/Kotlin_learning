package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleRemoteResponse
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.git.inner_data.Point
import ir.hrka.kotlin.domain.entities.git.PointData
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadGitPointsRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadPointsRepo {

    override suspend fun getPoints(topic: Topic): Flow<Result<List<Point>?, Errors>> =
        handleRemoteResponse(PointData::class.java) {
            gitAPI.getTopicPoints(topic.courseName, topic.fileName)
        }
}