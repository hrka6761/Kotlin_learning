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
import ir.hrka.kotlin.data.datasource.db.interactions.CoroutineTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.KotlinTopicsDao
import ir.hrka.kotlin.data.datasource.db.interactions.PointsDao
import ir.hrka.kotlin.data.datasource.db.interactions.SnippetCodesDao
import ir.hrka.kotlin.data.datasource.db.interactions.SubPointsDao
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
    fun provideKotlinTopicsDao(db: AppDatabase): KotlinTopicsDao = db.kotlinTopicsDao()

    @Singleton
    @Provides
    fun provideCoroutineTopicsDao(db: AppDatabase): CoroutineTopicsDao = db.coroutineTopicsDao()

    @Singleton
    @Provides
    fun providePointDao(db: AppDatabase): PointsDao = db.pointsDao()

    @Singleton
    @Provides
    fun provideSubPointDao(db: AppDatabase): SubPointsDao = db.supPointsDao()

    @Singleton
    @Provides
    fun provideSnippetCodeDao(db: AppDatabase): SnippetCodesDao = db.snippetCodesDao()
}