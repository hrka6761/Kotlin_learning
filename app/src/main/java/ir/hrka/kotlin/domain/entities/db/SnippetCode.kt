package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snippet_code")
data class SnippetCode (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "point_id") val pointId: Int,
    @ColumnInfo(name = "snippet_code_text") val snippetCodeText: String,
)