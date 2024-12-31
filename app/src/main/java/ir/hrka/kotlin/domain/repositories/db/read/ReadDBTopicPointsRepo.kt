package ir.hrka.kotlin.domain.repositories.db.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointData

interface ReadDBTopicPointsRepo {

    suspend fun getTopicPoints(topicName: String): Resource<List<PointData>?>
}