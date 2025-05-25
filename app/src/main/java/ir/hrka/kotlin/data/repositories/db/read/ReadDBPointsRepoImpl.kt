package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleDBResponse
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.git.inner_data.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ReadDBPointsRepoImpl @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val pointDao: PointDao,
    private val subPointDao: SubPointDao,
    private val snippetCodeDao: SnippetCodeDao
) : ReadPointsRepo {

    override suspend fun getPoints(topic: Topic): Flow<Result<List<Point>?, Errors>> =
        handleDBResponse {
            val pointsList = mutableListOf<Point>()
            val points = pointDao.getPoints(topic.topicTitle)

            points.forEach { point ->
                val subPointsDiffered = CoroutineScope(io).async {
                    point.id?.let { subPointDao.getSubPoints(it) }
                }
                val snippetCodesDiffered = CoroutineScope(io).async {
                    point.id?.let { snippetCodeDao.getSnippetCodes(it) }
                }

                val subPoints = subPointsDiffered.await()
                val snippetCodes = snippetCodesDiffered.await()

                pointsList.add(
                    Point(
                        point.id!!,
                        point.pointText,
                        subPoints?.map { subPoint -> subPoint.subPointText },
                        snippetCodes?.map { snippetCode -> snippetCode.snippetCodeText }
                    )
                )
            }

            return@handleDBResponse pointsList
        }
}