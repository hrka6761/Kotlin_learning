package ir.hrka.kotlin.data.datasource.db.interactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.SnippetCode

@Dao
interface SnippetCodesDao {

    @Insert
    suspend fun insertSnippetCodes(vararg snippetCode: SnippetCode)

    @Query("SELECT * FROM snippet_code where point_id = :pointId")
    suspend fun getSnippetCodes(pointId: Long): List<SnippetCode>

    @Query("DELETE FROM snippet_code where point_id = :pointId")
    suspend fun deleteSnippetCodes(pointId: Long)
}