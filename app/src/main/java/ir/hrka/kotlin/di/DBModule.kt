package ir.hrka.kotlin.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.core.utilities.Constants.DATABASE_NAME
import ir.hrka.kotlin.data.datasource.AppDatabase
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
}