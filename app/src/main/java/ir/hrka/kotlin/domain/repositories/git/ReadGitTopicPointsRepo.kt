package ir.hrka.kotlin.domain.repositories.git

import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointData

interface ReadGitTopicPointsRepo {

    suspend fun getTopicPoints(course: Course, topicName: String): Resource<List<PointData>?>
}