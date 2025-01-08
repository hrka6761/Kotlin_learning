package ir.hrka.kotlin.domain.repositories.read

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic

interface ReadPointsRepo {

    suspend fun getPoints(topic: Topic): Resource<List<Point>?>
}