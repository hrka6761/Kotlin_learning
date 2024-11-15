package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_point")
data class SubPoint (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "point_id") val pointId: Int,
    @ColumnInfo(name = "sub_point_text") val subPointText: String,
)