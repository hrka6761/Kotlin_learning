package ir.hrka.kotlin.data.repositories.db.read

import ir.hrka.kotlin.core.Constants.DB_READ_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class ReadDBPointsRepoImpl @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val pointDao: PointDao,
    private val subPointDao: SubPointDao,
    private val snippetCodeDao: SnippetCodeDao
) : ReadPointsRepo {

    override suspend fun getPoints(topic: Topic): Resource<List<Point>?> {
        val pointsList = mutableListOf<Point>()
        val points = pointDao.getPoints(topic.topicTitle)

        return try {
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

            Resource.Success(pointsList)
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_POINTS_ERROR_CODE,
                    errorMsg = "Can't read Points from the database."
                )
            )
        }
    }
}