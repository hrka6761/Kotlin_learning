package ir.hrka.kotlin.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.PointsDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodesDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointsDao
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic
import ir.hrka.kotlin.domain.entities.db.KotlinTopic
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint

@Database(
    entities = [
        KotlinTopic::class,
        CoroutineTopic::class,
        Point::class,
        SubPoint::class,
        SnippetCode::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kotlinTopicsDao(): KotlinTopicsDao
    abstract fun coroutineTopicsDao(): CoroutineTopicsDao
    abstract fun pointsDao(): PointsDao
    abstract fun supPointsDao(): SubPointsDao
    abstract fun snippetCodesDao(): SnippetCodesDao
}