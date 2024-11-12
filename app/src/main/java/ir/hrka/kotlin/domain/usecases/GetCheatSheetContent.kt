package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.core.utilities.Constants.READ_FILE_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.decodeBase64
import ir.hrka.kotlin.core.utilities.extractClearPointsFromRawPoint
import ir.hrka.kotlin.core.utilities.extractJavaDocsFromCheatSheetFileContent
import ir.hrka.kotlin.core.utilities.extractHeadPointsFromPointContent
import ir.hrka.kotlin.core.utilities.extractRawPointsFromJavaDocContent
import ir.hrka.kotlin.core.utilities.extractSnippetCodeFromPoint
import ir.hrka.kotlin.core.utilities.extractSubPointsFromPointContent
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import javax.inject.Inject

class GetCheatSheetContent @Inject constructor(private val cheatSheetsRepo: CheatSheetsRepo) {

    suspend operator fun invoke(fileName: String): Resource<List<PointDataModel>?> {
        val cheatSheetFile = cheatSheetsRepo.getCheatSheetFile(fileName).data
        val encodedCheatSheetContent = cheatSheetFile?.content ?: ""
        val decodedCheatSheetContent = encodedCheatSheetContent.decodeBase64()

        return if (decodedCheatSheetContent.isNotEmpty())
            Resource.Success(provideCheatSheetData(decodedCheatSheetContent))
        else
            Resource.Error(
                ErrorModel(
                    READ_FILE_ERROR_CODE,
                    "Can't access the github repository files."
                )
            )
    }


    private fun provideCheatSheetData(decodedCheatSheetContent: String): List<PointDataModel> {
        val content = decodedCheatSheetContent.extractJavaDocsFromCheatSheetFileContent()
        val list = mutableListOf<PointDataModel>()
        var index = 1

        content.forEach { javaDoc ->
            javaDoc.extractRawPointsFromJavaDocContent()
                .forEach { rawPoint ->
                    val clearPoint = rawPoint.extractClearPointsFromRawPoint()
                    val snippets = rawPoint.extractSnippetCodeFromPoint()
                    val headPoint = clearPoint.extractHeadPointsFromPointContent()
                    if (headPoint.isNotEmpty()) {
                        val subPoint = clearPoint.extractSubPointsFromPointContent()
                        list.add(
                            PointDataModel(
                                index++,
                                rawPoint,
                                headPoint,
                                subPoint,
                                snippets
                            )
                        )
                    }
                }
        }

        return list
    }
}