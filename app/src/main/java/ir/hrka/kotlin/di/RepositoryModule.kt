package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.ReadDBCheatSheetsRepoImpl
import ir.hrka.kotlin.data.repositories.ReadGithubCheatSheetsRepoImpl
import ir.hrka.kotlin.data.repositories.WriteDBCheatSheetsRepoImpl
import ir.hrka.kotlin.data.repositories.LocalDataRepoImpl
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.CheatSheetsRepo
import ir.hrka.kotlin.domain.repositories.DBRepo
import ir.hrka.kotlin.domain.repositories.LocalDataRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo

    @Named("Github")
    @Binds
    fun bindGithubCheatSheetsListRepo(readGithubCheatSheetsRepoImpl: ReadGithubCheatSheetsRepoImpl): CheatSheetsRepo

    @Named("db")
    @Binds
    fun bindDBCheatSheetsListRepo(ReadDBCheatSheetsRepoImpl: ReadDBCheatSheetsRepoImpl): CheatSheetsRepo

    @Binds
    fun bindLocalDataRepo(localDataRepoImpl: LocalDataRepoImpl): LocalDataRepo

    @Binds
    fun bindDBRepo(writeDbCheatSheetsRepoImpl: WriteDBCheatSheetsRepoImpl): DBRepo
}