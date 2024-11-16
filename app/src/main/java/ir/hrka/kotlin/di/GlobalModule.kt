package ir.hrka.kotlin.di

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GlobalModule {

    @Named("Main")
    @Provides
    fun provideMainDispatchers(): CoroutineDispatcher = Dispatchers.Main

    @Named("IO")
    @Provides
    fun provideIODispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Named("Default")
    @Provides
    fun provideDefaultDispatchers(): CoroutineDispatcher = Dispatchers.Default

    @Named("Unconfined")
    @Provides
    fun provideUnConfinedDispatchers(): CoroutineDispatcher = Dispatchers.Unconfined

    @Provides
    fun provideDownloadManager(
        @ApplicationContext context: Context
    ): DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor =
        sharedPreferences.edit()
}