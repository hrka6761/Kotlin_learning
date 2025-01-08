package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.DBPoint

@Dao
interface PointDao {

    @Insert
    suspend fun insertPoints(point: DBPoint): Long

    @Query("SELECT * FROM point where topic_name = :topicName")
    suspend fun getPoints(topicName: String): List<DBPoint>

    @Query("DELETE FROM point where id = :id")
    suspend fun deletePoints(id: Long)
}