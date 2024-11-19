package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.ReadDBCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.ReadGithubCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.WriteDBCheatsheetRepoImpl
import ir.hrka.kotlin.data.repositories.LocalDataRepoImpl
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.ReadCheatSheetRepo
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import ir.hrka.kotlin.domain.repositories.LocalDataRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo

    @Named("Github")
    @Binds
    fun bindGithubCheatSheetsListRepo(readGithubCheatSheetsRepoImpl: ReadGithubCheatSheetRepoImpl): ReadCheatSheetRepo

    @Named("db")
    @Binds
    fun bindDBCheatSheetsListRepo(readDBCheatSheetsRepoImpl: ReadDBCheatSheetRepoImpl): ReadCheatSheetRepo

    @Binds
    fun bindLocalDataRepo(localDataRepoImpl: LocalDataRepoImpl): LocalDataRepo

    @Binds
    fun bindDBRepo(writeDbCheatSheetsRepoImpl: WriteDBCheatsheetRepoImpl): WriteCheatsheetRepo
}