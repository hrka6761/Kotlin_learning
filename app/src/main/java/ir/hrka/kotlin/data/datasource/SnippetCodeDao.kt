package ir.hrka.kotlin.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.SnippetCode

@Dao
interface SnippetCodeDao {

    @Insert
    suspend fun insertSnippetCodes(vararg snippetCode: SnippetCode)

    @Query("SELECT * FROM snippet_code where point_id = :pointId")
    suspend fun getPointSnippetCodes(pointId: Long): List<SnippetCode>

    @Query("DELETE FROM snippet_code where point_id = :pointId")
    suspend fun deletePointSnippetCodes(pointId: Long)
}