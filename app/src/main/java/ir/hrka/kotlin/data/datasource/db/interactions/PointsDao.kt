package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Point

@Dao
interface PointsDao {

    @Insert
    suspend fun insertPoints(point: Point): Long

    @Query("SELECT * FROM point where topic_name = :topicName")
    suspend fun getPoints(topicName: String): List<Point>

    @Query("DELETE FROM point where topic_name = :topicName")
    suspend fun deletePoints(topicName: String)
}