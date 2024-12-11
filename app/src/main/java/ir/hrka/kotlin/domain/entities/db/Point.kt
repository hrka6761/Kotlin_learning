package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class Point(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "point_text") val pointText: String,
    @ColumnInfo(name = "topic_name") val topicName: String
)