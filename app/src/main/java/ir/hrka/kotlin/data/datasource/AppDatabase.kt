package ir.hrka.kotlin.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hrka.kotlin.domain.entities.db.Cheatsheet
import ir.hrka.kotlin.domain.entities.db.Point
import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.entities.db.SubPoint

@Database(
    entities = [Cheatsheet::class, Point::class, SubPoint::class, SnippetCode::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cheatsheetDao(): CheatsheetDao
    abstract fun pointDao(): PointDao
    abstract fun supPointDao(): SubPointDao
    abstract fun snippetCodeDao(): SnippetCodeDao
}