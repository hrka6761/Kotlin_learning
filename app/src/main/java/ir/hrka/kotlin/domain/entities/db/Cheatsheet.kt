package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cheatsheet")
data class Cheatsheet(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "version_name") val versionName: String,
    @ColumnInfo(name = "has_content_updated") val hasContentUpdated: Boolean = true,
)
