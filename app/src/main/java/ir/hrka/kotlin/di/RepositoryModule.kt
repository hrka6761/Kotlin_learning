package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.github.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.db.ReadDBCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.github.ReadGithubCheatSheetRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBCheatsheetRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionDataRepoImpl
import ir.hrka.kotlin.domain.repositories.github.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.github.ReadGithubCheatSheetRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBCheatsheetRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.db.ReadDBCheatSheetRepo

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