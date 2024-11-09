package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.CheatSheetRepoImpl
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo

    @Binds
    fun bindCheatSheetsListRepo(cheatSheetRepoImpl: CheatSheetRepoImpl): CheatSheetsRepo
}