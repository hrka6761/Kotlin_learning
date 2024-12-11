package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic

@Dao
interface CoroutineTopicsDao {

    @Insert
    suspend fun insertTopics(vararg coroutineTopic: CoroutineTopic)

    @Query("SELECT * FROM coroutine_topic")
    suspend fun getTopics(): List<CoroutineTopic>

    @Query("DELETE FROM coroutine_topic")
    suspend fun deleteTopics()

    @Query("UPDATE coroutine_topic SET has_updated = :hasUpdated WHERE id = :id")
    suspend fun updateTopicState(id: Int, hasUpdated: Boolean)
}