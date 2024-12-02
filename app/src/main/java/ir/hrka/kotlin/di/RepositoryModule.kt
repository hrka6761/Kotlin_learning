package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.ReadDBCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.ReadGithubCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.WriteDBCheatsheetRepoImpl
import ir.hrka.kotlin.data.repositories.VersionDataRepoImpl
import ir.hrka.kotlin.domain.repositories.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.ReadGithubCheatSheetRepo
import ir.hrka.kotlin.domain.repositories.WriteDBCheatsheetRepo
import ir.hrka.kotlin.domain.repositories.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.ReadDBCheatSheetRepo

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo

    @Binds
    fun bindGithubCheatSheetsListRepo(readGithubCheatSheetsRepoImpl: ReadGithubCheatSheetRepoImpl): ReadGithubCheatSheetRepo

    @Binds
    fun bindDBCheatSheetsListRepo(readDBCheatSheetsRepoImpl: ReadDBCheatSheetRepoImpl): ReadDBCheatSheetRepo

    @Binds
    fun bindLocalDataRepo(localDataRepoImpl: VersionDataRepoImpl): VersionDataRepo

    @Binds
    fun bindDBRepo(writeDbCheatSheetsRepoImpl: WriteDBCheatsheetRepoImpl): WriteDBCheatsheetRepo
}