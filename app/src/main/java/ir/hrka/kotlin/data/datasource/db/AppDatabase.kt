package ir.hrka.kotlin.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint

@Database(
    entities = [
        Course::class,
        Topic::class,
        Point::class,
        SubPoint::class,
        SnippetCode::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coursesDao(): CourseDao
    abstract fun kotlinTopicsDao(): TopicDao
    abstract fun pointsDao(): PointDao
    abstract fun supPointsDao(): SubPointDao
    abstract fun snippetCodesDao(): SnippetCodeDao
}