package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.SubPoint

@Dao
interface SubPointDao {

    @Insert
    suspend fun insertSubPoints(vararg subPoints: SubPoint)

    @Query("SELECT * FROM sub_point where point_id = :pointId")
    suspend fun getSubPoints(pointId: Long): List<SubPoint>

    @Query("DELETE FROM sub_point where point_id = :pointId")
    suspend fun deleteSubPoints(pointId: Long)
}