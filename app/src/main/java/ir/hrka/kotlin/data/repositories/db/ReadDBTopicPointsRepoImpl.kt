package ir.hrka.kotlin.data.repositories.db

import ir.hrka.kotlin.core.Constants.DB_READ_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.interactions.PointsDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodesDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointsDao
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicPointsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class ReadDBTopicPointsRepoImpl @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val pointDao: PointsDao,
    private val subPointDao: SubPointsDao,
    private val snippetCodeDao: SnippetCodesDao
) : ReadDBTopicPointsRepo {

    override suspend fun getTopicPoints(topicName: String): Resource<List<PointData>?> {
        return try {
            val points = pointDao.getPoints(topicName)
            var index = 1

            return Resource.Success(
                points.map { point ->
                    val subPointsDiffered = CoroutineScope(io).async {
                        point.id?.let { subPointDao.getSubPoints(it) }
                    }
                    val snippetsCodesDiffered = CoroutineScope(io).async {
                        point.id?.let { snippetCodeDao.getSnippetCodes(it) }
                    }

                    val subPoints =
                        subPointsDiffered.await()
                            ?.map { subPoint -> subPoint.subPointText }
                    val snippetsCodes =
                        snippetsCodesDiffered.await()
                            ?.map { snippetsCode -> snippetsCode.snippetCodeText }

                    PointData(
                        num = index++,
                        databaseId = point.id,
                        rawPoint = "",
                        headPoint = point.pointText,
                        subPoints = subPoints,
                        snippetsCode = snippetsCodes
                    )
                }
            )
        } catch (e: Exception) {
            Resource.Error(
                Error(
                    errorCode = DB_READ_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}