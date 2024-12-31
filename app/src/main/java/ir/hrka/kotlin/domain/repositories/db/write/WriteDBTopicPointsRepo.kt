package ir.hrka.kotlin.domain.repositories.db.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint

interface WriteDBTopicPointsRepo {

    suspend fun savePointOnDB(point: Point): Resource<Long>
    suspend fun saveSubPointsOnDB(subPoints: Array<SubPoint>): Resource<Boolean>
    suspend fun saveSnippetCodesOnDB(snippetCodes: Array<SnippetCode>): Resource<Boolean>
    suspend fun deletePoints(topicName: String): Resource<Boolean>
    suspend fun deleteSubPoints(pointId: Long): Resource<Boolean>
    suspend fun deleteSnippetCodes(pointId: Long): Resource<Boolean>
}