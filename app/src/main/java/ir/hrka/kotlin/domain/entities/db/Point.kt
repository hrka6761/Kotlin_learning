package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class Point(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "point_text") val pointText: String,
    @ColumnInfo(name = "version_name") val versionName: String,
    @ColumnInfo(name = "cheatsheet_name") val cheatsheetName: String
)