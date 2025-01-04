package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Topic

@Dao
interface TopicDao {

    @Insert
    suspend fun insertTopics(vararg topics: Topic)

    @Query("SELECT * FROM topic WHERE course_name = :courseName")
    suspend fun getTopics(courseName: String): List<Topic>

    @Query("DELETE FROM topic WHERE course_name = :courseName")
    suspend fun deleteTopics(courseName: String)

    @Query("UPDATE topic SET has_update = :hasUpdated WHERE topic_id = :id")
    suspend fun updateTopicState(id: Int, hasUpdated: Boolean)
}