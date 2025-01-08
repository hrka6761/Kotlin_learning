package ir.hrka.kotlin.domain.usecases.db.points

import ir.hrka.kotlin.core.Constants.DB_WRITE_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.errors.Error
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.DBPoint
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import ir.hrka.kotlin.domain.repositories.write.WritePointsRepo
import ir.hrka.kotlin.domain.repositories.write.WriteSnippetCodesRepo
import ir.hrka.kotlin.domain.repositories.write.WriteSubPointsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class UpdatePointsOnDBUseCase @Inject constructor(
    @Named("Default") private val default: CoroutineDispatcher,
    @Named("db")private val readPointsRepo: ReadPointsRepo,
    private val writePointsRepo: WritePointsRepo,
    private val writeSubPointsRepo: WriteSubPointsRepo,
    private val writeSnippetCodesRepo: WriteSnippetCodesRepo
) {

    suspend operator fun invoke(topicPoints: List<Point>, topic: Topic): Resource<Boolean?> {
        val getOldPointsResult = readPointsRepo.getPoints(topic)

        if (getOldPointsResult is Resource.Error)
            return Resource.Error(getOldPointsResult.error!!)

        val oldPoints = getOldPointsResult.data

        if (oldPoints.isNullOrEmpty()) {
            val saveResult = saveNewPointsOnDb(topicPoints, topic.topicTitle)

            if (saveResult is Resource.Error)
                return saveResult
        } else {
            val removeResult = removeOldPointFromDB(oldPoints)

            if (removeResult is Resource.Error)
                return removeResult

            val saveResult = saveNewPointsOnDb(topicPoints, topic.topicTitle)

            if (saveResult is Resource.Error)
                return saveResult
        }

        return Resource.Success(true)
    }


    private suspend fun removeOldPointFromDB(
        oldPoints: List<Point>
    ): Resource<Boolean?> {
        oldPoints.forEach { oldPoint ->
            val removePointDiffered =
                CoroutineScope(default).async { writePointsRepo.removePoint(oldPoint.id) }
            val removeSubPointDiffered =
                CoroutineScope(default).async { writeSubPointsRepo.removeSubPoints(oldPoint.id) }
            val removeSnippetCodeDiffered =
                CoroutineScope(default).async { writeSnippetCodesRepo.removeSnippetCodes(oldPoint.id) }

            val removePointResult = removePointDiffered.await()
            val removeSubPointResult = removeSubPointDiffered.await()
            val removeSnippetCodeResult = removeSnippetCodeDiffered.await()

            if (removePointResult is Resource.Error)
                return removePointResult

            if (removeSubPointResult is Resource.Error)
                return removeSubPointResult

            if (removeSnippetCodeResult is Resource.Error)
                return removeSnippetCodeResult
        }

        return Resource.Success(true)
    }

    private suspend fun saveNewPointsOnDb(
        newPoints: List<Point>,
        topicTitle: String
    ): Resource<Boolean?> {
        newPoints.forEach { newPoint ->
            val savePointDiffered =
                CoroutineScope(default).async {
                    writePointsRepo.savePoint(
                        DBPoint(
                            pointText = newPoint.headPoint,
                            topicName = topicTitle
                        )
                    )
                }

            val savePointResult = savePointDiffered.await()

            if (savePointResult is Resource.Error)
                return Resource.Error(savePointResult.error!!)

            val rowId = savePointResult.data

            if (rowId != null) {
                val saveSubPointDiffered =
                    CoroutineScope(default).async {
                        newPoint.subPoints?.map { str ->
                            SubPoint(
                                pointId = rowId,
                                subPointText = str
                            )
                        }?.toTypedArray()?.let { writeSubPointsRepo.saveSubPoints(it) }
                    }

                val saveSnippetCodeDiffered =
                    CoroutineScope(default).async {
                        newPoint.snippetsCodes?.map { str ->
                            SnippetCode(
                                pointId = rowId,
                                snippetCodeText = str
                            )
                        }?.toTypedArray()?.let { writeSnippetCodesRepo.saveSnippetCodes(it) }
                    }

                val saveSubPointResult = saveSubPointDiffered.await()
                val saveSnippetCodeResult = saveSnippetCodeDiffered.await()

                if (saveSubPointResult is Resource.Error)
                    return saveSubPointResult

                if (saveSnippetCodeResult is Resource.Error)
                    return saveSnippetCodeResult

            } else
                return Resource.Error(
                    Error(
                        errorCode = DB_WRITE_POINTS_ERROR_CODE,
                        errorMsg = "Can't save sub Points and snippet codes when Point id is null."
                    )
                )
        }

        return Resource.Success(true)
    }
}