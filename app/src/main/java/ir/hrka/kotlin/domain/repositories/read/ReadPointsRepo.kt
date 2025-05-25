package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.git.inner_data.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import kotlinx.coroutines.flow.Flow

interface ReadPointsRepo {

    suspend fun getPoints(topic: Topic): Flow<Result<List<Point>?, Errors>>
}