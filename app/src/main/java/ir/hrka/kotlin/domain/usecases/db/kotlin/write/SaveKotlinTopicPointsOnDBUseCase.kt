package ir.hrka.kotlin.domain.usecases.db.kotlin.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.PointData
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBTopicPointsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class SaveKotlinTopicPointsOnDBUseCase @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val writeDBTopicPointsRepo: WriteDBTopicPointsRepo,
    private val readDBTopicPointsRepo: ReadDBTopicPointsRepo
) {

    suspend operator fun invoke(points: List<PointData>, topicName: String): Resource<Boolean> {

        var totalResult: Resource<Boolean> = Resource.Success(true)
        val deleteResult = deleteKotlinTopicPoints(topicName)

        if (deleteResult is Resource.Error) {
            totalResult = deleteResult

            return totalResult
        }

        points.forEach { pointDataModel ->

            val savePointDiffered = CoroutineScope(io).async {
                writeDBTopicPointsRepo.savePointOnDB(
                    Point(
                        pointText = pointDataModel.headPoint,
                        topicName = topicName
                    )
                )
            }

            val savePointResult = savePointDiffered.await()

            if (savePointResult is Resource.Error) {
                totalResult = Resource.Error(savePointResult.error!!)
                return@forEach
            }

            val saveSubPointsDiffered = CoroutineScope(io).async {
                pointDataModel.subPoints
                    ?.map { str ->
                        SubPoint(
                            pointId = savePointResult.data!!,
                            subPointText = str
                        )
                    }
                    ?.toTypedArray()
                    ?.let {
                        writeDBTopicPointsRepo.saveSubPointsOnDB(it)
                    }
            }

            val saveSnippetCodesDiffered = CoroutineScope(io).async {
                pointDataModel.snippetsCode
                    ?.map { str ->
                        SnippetCode(
                            pointId = savePointResult.data!!,
                            snippetCodeText = str
                        )
                    }
                    ?.toTypedArray()
                    ?.let {
                        writeDBTopicPointsRepo.saveSnippetCodesOnDB(it)
                    }
            }

            val saveSubPointsResult = saveSubPointsDiffered.await()

            val saveSnippetCodesResult = saveSnippetCodesDiffered.await()

            if (saveSubPointsResult is Resource.Error) {
                totalResult = saveSubPointsResult
                return@forEach
            }

            if (saveSnippetCodesResult is Resource.Error) {
                totalResult = saveSnippetCodesResult
                return@forEach
            }
        }

        return totalResult
    }


    private suspend fun deleteKotlinTopicPoints(topicName: String): Resource<Boolean> {
        val cheatsheetPointsIdResult =
            readDBTopicPointsRepo.getTopicPoints(topicName)

        if (cheatsheetPointsIdResult is Resource.Error)
            return Resource.Error(cheatsheetPointsIdResult.error!!)

        val cheatsheets = cheatsheetPointsIdResult.data
            ?.map { pointDataModel -> pointDataModel.databaseId!! }!!

        val deletePointsDiffered = CoroutineScope(io).async {
            writeDBTopicPointsRepo.deletePoints(topicName)
        }

        val deleteSubPointsDiffered = CoroutineScope(io).async {
            var result: Resource<Boolean> = Resource.Success(true)

            cheatsheets.forEach {
                result = writeDBTopicPointsRepo.deleteSubPoints(it)
                if (result is Resource.Error) {
                    return@forEach
                }
            }

            result
        }

        val deleteSnippetCodesDiffered = CoroutineScope(io).async {
            var result: Resource<Boolean> = Resource.Success(true)

            cheatsheets.forEach {
                result = writeDBTopicPointsRepo.deleteSnippetCodes(it)

                if (result is Resource.Error) {
                    return@forEach
                }
            }

            result
        }

        val deletePointsResult = deletePointsDiffered.await()
        val deleteSubPointsResult = deleteSubPointsDiffered.await()
        val deleteSnippetCodesResult = deleteSnippetCodesDiffered.await()

        if (deletePointsResult is Resource.Error) {
            return deletePointsResult
        }
        if (deleteSubPointsResult is Resource.Error) {
            return deleteSubPointsResult
        }
        if (deleteSnippetCodesResult is Resource.Error) {
            return deleteSnippetCodesResult
        }

        return Resource.Success(true)
    }
}