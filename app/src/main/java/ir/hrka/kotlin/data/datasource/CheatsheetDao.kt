package ir.hrka.kotlin.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.hrka.kotlin.domain.entities.db.Cheatsheet

@Dao
interface CheatsheetDao {

    @Insert
    suspend fun insertCheatsheets(vararg cheatsheets: Cheatsheet)

    @Query("SELECT * FROM cheatsheet")
    suspend fun getCheatsheets(): List<Cheatsheet>

    @Query("DELETE FROM cheatsheet")
    suspend fun deleteCheatsheets()

    @Query("UPDATE cheatsheet SET has_content_updated = :hasContentUpdated WHERE id = :id")
    suspend fun updateCheatsheetUpdateState(id: Int, hasContentUpdated: Boolean)
}