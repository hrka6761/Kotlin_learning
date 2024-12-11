package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.db.ReadDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.git.AppInfoRepoImpl
import ir.hrka.kotlin.data.repositories.db.ReadDBTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBCoroutineTopicRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBKotlinTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionDataRepoImpl
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.git.AppInfoRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoroutineTopicRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: AppInfoRepoImpl): AppInfoRepo

    @Binds
    fun bindReadGitTopicsRepo(readGitTopicsRepoImpl: ReadGitTopicsRepoImpl): ReadGitTopicsRepo

    @Binds
    fun bindReadGitTopicPointsRepo(readGitTopicPointsRepoImpl: ReadGitTopicPointsRepoImpl): ReadGitTopicPointsRepo

    @Binds
    fun bindVersionDataRepo(versionDataRepoImpl: VersionDataRepoImpl): VersionDataRepo

    @Binds
    fun bindReadDBTopicsRepo(readDBTopicsRepoImpl: ReadDBTopicsRepoImpl): ReadDBTopicsRepo

    @Binds
    fun bindReadDBTopicPointsRepo(readDBTopicPointsRepoImpl: ReadDBTopicPointsRepoImpl): ReadDBTopicPointsRepo

    @Binds
    fun bindWriteDBKotlinTopicsRepo(writeDBKotlinTopicsRepoImpl: WriteDBKotlinTopicsRepoImpl): WriteDBKotlinTopicsRepo

    @Binds
    fun bindWriteDBCoroutineTopicRepo(writeDBCoroutineTopicRepoImpl: WriteDBCoroutineTopicRepoImpl): WriteDBCoroutineTopicRepo

    @Binds
    fun bindWriteDBTopicPointsRepo(writeDBTopicPointsRepoImpl: WriteDBTopicPointsRepoImpl): WriteDBTopicPointsRepo
}