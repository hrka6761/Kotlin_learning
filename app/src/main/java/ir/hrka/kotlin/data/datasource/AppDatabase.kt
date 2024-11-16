package ir.hrka.kotlin.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.hrka.kotlin.domain.entities.db.Cheatsheet

@Database(entities = [Cheatsheet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cheatsheetDao(): CheatsheetDao
}