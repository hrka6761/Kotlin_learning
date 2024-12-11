package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.KotlinTopic

@Dao
interface KotlinTopicsDao {

    @Insert
    suspend fun insertTopics(vararg kotlinTopics: KotlinTopic)

    @Query("SELECT * FROM kotlin_topic")
    suspend fun getTopics(): List<KotlinTopic>

    @Query("DELETE FROM kotlin_topic")
    suspend fun deleteTopics()

    @Query("UPDATE kotlin_topic SET has_updated = :hasUpdated WHERE id = :id")
    suspend fun updateTopicState(id: Int, hasUpdated: Boolean)
}