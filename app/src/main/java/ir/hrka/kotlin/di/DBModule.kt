package ir.hrka.kotlin.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.core.Constants.DATABASE_NAME
import ir.hrka.kotlin.data.datasource.db.AppDatabase
import ir.hrka.kotlin.data.datasource.db.interactions.CourseDao
import ir.hrka.kotlin.data.datasource.db.interactions.TopicDao
import ir.hrka.kotlin.data.datasource.db.interactions.PointDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodeDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideCoursesDao(db: AppDatabase): CourseDao = db.coursesDao()

    @Singleton
    @Provides
    fun provideKotlinTopicsDao(db: AppDatabase): TopicDao = db.kotlinTopicsDao()

    @Singleton
    @Provides
    fun providePointDao(db: AppDatabase): PointDao = db.pointsDao()

    @Singleton
    @Provides
    fun provideSubPointDao(db: AppDatabase): SubPointDao = db.supPointsDao()

    @Singleton
    @Provides
    fun provideSnippetCodeDao(db: AppDatabase): SnippetCodeDao = db.snippetCodesDao()
}