package ir.hrka.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

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
}