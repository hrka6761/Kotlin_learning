package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.AppInfoRepoImpl
import ir.hrka.kotlin.domain.repositories.AppInfoRepo

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo
}