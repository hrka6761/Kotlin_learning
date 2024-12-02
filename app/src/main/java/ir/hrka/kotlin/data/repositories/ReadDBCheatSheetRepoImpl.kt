package ir.hrka.kotlin.data.repositories

import ir.hrka.kotlin.core.Constants.DB_READ_CHEATSHEETS_LIST_ERROR_CODE
import ir.hrka.kotlin.core.Constants.DB_READ_CHEATSHEET_POINTS_ERROR_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.data.datasource.db.dbinteractions.CheatsheetDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.PointDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.SubPointDao
import ir.hrka.kotlin.domain.entities.ErrorModel
import ir.hrka.kotlin.domain.entities.PointDataModel
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.repositories.ReadDBCheatSheetRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class ReadDBCheatSheetRepoImpl @Inject constructor(
    @Named("IO") private val io: CoroutineDispatcher,
    private val cheatsheetDao: CheatsheetDao,
    private val pointDao: PointDao,
    private val subPointDao: SubPointDao,
    private val snippetCodeDao: SnippetCodeDao
) : ReadDBCheatSheetRepo {

    override suspend fun getCheatSheetsList(): Resource<List<Cheatsheet>?> {
        return try {
            Resource.Success(cheatsheetDao.getCheatsheets())
        } catch (e: Exception) {
            Resource.Error(
                ErrorModel(
                    errorCode = DB_READ_CHEATSHEETS_LIST_ERROR_CODE,
                    errorMsg = "Can't read cheatsheet from the database."
                )
            )
        }
    }

    override suspend fun getCheatSheetPoints(cheatsheetName: String): Resource<List<PointDataModel>?> {
        return try {
            val points = pointDao.getCheatsheetPoints(cheatsheetName)
            var index = 1

            return Resource.Success(
                points.map { point ->
                    val subPointsDiffered = CoroutineScope(io).async {
                        point.id?.let { subPointDao.getPointSubPoints(it) }
                    }
                    val snippetsCodesDiffered = CoroutineScope(io).async {
                        point.id?.let { snippetCodeDao.getPointSnippetCodes(it) }
                    }

                    val subPoints =
                        subPointsDiffered.await()?.map { subPoint -> subPoint.subPointText }
                    val snippetsCodes =
                        snippetsCodesDiffered.await()
                            ?.map { snippetsCode -> snippetsCode.snippetCodeText }

                    PointDataModel(
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
                ErrorModel(
                    errorCode = DB_READ_CHEATSHEET_POINTS_ERROR_CODE,
                    errorMsg = e.message.toString()
                )
            )
        }
    }
}