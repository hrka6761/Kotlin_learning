package ir.hrka.kotlin.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hrka.kotlin.data.datasource.db.dbinteractions.CheatsheetDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.PointDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.dbinteractions.SubPointDao
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