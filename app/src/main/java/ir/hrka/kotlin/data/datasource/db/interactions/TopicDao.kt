package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Topic

@Dao
interface TopicDao {

    @Insert
    suspend fun insertTopics(vararg topics: Topic)

    @Query("SELECT * FROM topic")
    suspend fun getTopics(): List<Topic>

    @Query("DELETE FROM topic")
    suspend fun deleteTopics()

    @Query("UPDATE topic SET has_update = :hasUpdated WHERE topic_db_id = :topicDBId")
    suspend fun updateTopicState(topicDBId: Int, hasUpdated: Boolean)
}