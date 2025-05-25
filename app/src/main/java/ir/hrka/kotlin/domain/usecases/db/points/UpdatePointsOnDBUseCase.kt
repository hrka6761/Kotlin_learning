package ir.hrka.kotlin.domain.usecases.db.points

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.DBPoint
import ir.hrka.kotlin.domain.entities.git.inner_data.Point
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject
import javax.inject.Named

class UpdatePointsOnDBUseCase @Inject constructor(
    @Named("Default") private val default: CoroutineDispatcher,
    @Named("db") private val readPointsRepo: ReadPointsRepo,
    private val writePointsRepo: WritePointsRepo,
    private val writeSubPointsRepo: WriteSubPointsRepo,
    private val writeSnippetCodesRepo: WriteSnippetCodesRepo
) {

    operator fun invoke(
        topicPoints: List<Point>,
        topic: Topic
    ): Flow<Result<Boolean?, Errors>> {
        var readyToRemove = false
        var readyToSave = false
        var oldPoints: List<Point>? = null

        return flow {
            readPointsRepo.getPoints(topic).collect { result ->
                if (result is Result.Loading || result is Result.Error) emit(result)
                if (result is Result.Success) {
                    readyToRemove = !result.data.isNullOrEmpty()
                    readyToSave = result.data.isNullOrEmpty()

                    if (!result.data.isNullOrEmpty()) oldPoints = result.data
                }
            }

            if (readyToRemove) {
                val removeResult = removeOldPointFromDB(oldPoints)
                readyToSave = removeResult is Result.Success

                if (removeResult is Result.Error) emit(removeResult)
            }

            if (readyToSave) emit(saveNewPointsOnDb(topicPoints, topic.topicTitle))
        }
    }


    private suspend fun removeOldPointFromDB(oldPoints: List<Point>?): Result<Boolean?, Errors> {
        oldPoints?.forEach { oldPoint ->
            val removePointDiffered =
                CoroutineScope(default).async {
                    writePointsRepo.removePoint(oldPoint.id).last()
                }
            val removeSubPointDiffered =
                CoroutineScope(default).async {
                    writeSubPointsRepo.removeSubPoints(oldPoint.id).last()
                }
            val removeSnippetCodeDiffered =
                CoroutineScope(default).async {
                    writeSnippetCodesRepo.removeSnippetCodes(oldPoint.id).last()
                }

            val removePointResult = removePointDiffered.await()
            val removeSubPointResult = removeSubPointDiffered.await()
            val removeSnippetCodeResult = removeSnippetCodeDiffered.await()

            if (removePointResult is Result.Error)
                return removePointResult

            if (removeSubPointResult is Result.Error)
                return removeSubPointResult

            if (removeSnippetCodeResult is Result.Error)
                return removeSnippetCodeResult
        }

        return Result.Success(true)
    }

    private suspend fun saveNewPointsOnDb(
        newPoints: List<Point>,
        topicTitle: String
    ): Result<Boolean?, Errors> {
        newPoints.forEach { newPoint ->
            val savePointDiffered =
                CoroutineScope(default).async {
                    writePointsRepo.savePoint(
                        DBPoint(
                            pointText = newPoint.headPoint,
                            topicName = topicTitle
                        )
                    ).last()
                }

            val savePointResult = savePointDiffered.await()

            if (savePointResult is Result.Error)
                return Result.Error(savePointResult.error)

            (savePointResult as Result.Success).data?.let { rowId ->
                val saveSubPointDiffered =
                    CoroutineScope(default).async {
                        newPoint.subPoints?.map { str ->
                            SubPoint(
                                pointId = rowId,
                                subPointText = str
                            )
                        }?.toTypedArray()?.let {
                            writeSubPointsRepo.saveSubPoints(it)
                        }?.last()
                    }

                val saveSnippetCodeDiffered =
                    CoroutineScope(default).async {
                        newPoint.snippetsCodes?.map { str ->
                            SnippetCode(
                                pointId = rowId,
                                snippetCodeText = str
                            )
                        }?.toTypedArray()?.let {
                            writeSnippetCodesRepo.saveSnippetCodes(it)
                        }?.last()
                    }

                val saveSubPointResult = saveSubPointDiffered.await()
                val saveSnippetCodeResult = saveSnippetCodeDiffered.await()

                if (saveSubPointResult is Result.Error)
                    return saveSubPointResult

                if (saveSnippetCodeResult is Result.Error)
                    return saveSnippetCodeResult
            }
        }

        return Result.Success(true)
    }
}