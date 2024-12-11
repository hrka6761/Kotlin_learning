package ir.hrka.kotlin.domain.entities.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coroutine_topic")
data class CoroutineTopic(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "title") val name: String,
    @ColumnInfo(name = "version_name") val versionName: String,
    @ColumnInfo(name = "has_visualizer") var hasVisualizer: Boolean = false,
    @ColumnInfo(name = "has_updated") var hasUpdated: Boolean = true,
)
