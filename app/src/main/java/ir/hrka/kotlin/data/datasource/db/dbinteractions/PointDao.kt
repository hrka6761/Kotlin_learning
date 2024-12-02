package ir.hrka.kotlin.data.datasource.db.dbinteractions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Point

@Dao
interface PointDao {

    @Insert
    suspend fun insertPoints(point: Point): Long

    @Query("SELECT * FROM point where cheatsheet_name = :cheatsheetName")
    suspend fun getCheatsheetPoints(cheatsheetName: String): List<Point>

    @Query("DELETE FROM point where cheatsheet_name = :cheatsheetName")
    suspend fun deleteCheatsheetPoints(cheatsheetName: String)
}