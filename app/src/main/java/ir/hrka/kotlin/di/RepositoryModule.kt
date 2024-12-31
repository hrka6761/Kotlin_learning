package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.db.read.ReadDBCoroutineTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.read.ReadDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.ChangelogRepoImpl
import ir.hrka.kotlin.data.repositories.db.read.ReadDBKotlinTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBCoroutineTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.WriteDBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.GitTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBKotlinTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.GetDBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.GetGitCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.GitTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionDataRepoImpl
import ir.hrka.kotlin.data.repositories.VersionInfoRepoImpl
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.ChangelogRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.WriteDBCoursesRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.GetCoursesRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.VersionInfoRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: ChangelogRepoImpl): ChangelogRepo

    @Binds
    fun bindReadGitTopicsRepo(gitTopicsRepoImpl: GitTopicsRepoImpl): ReadGitTopicsRepo

    @Binds
    fun bindReadGitTopicPointsRepo(gitTopicPointsRepoImpl: GitTopicPointsRepoImpl): ReadGitTopicPointsRepo

    @Binds
    fun bindVersionDataRepo(versionDataRepoImpl: VersionDataRepoImpl): VersionDataRepo

    @Binds
    fun bindReadDBKotlinTopicsRepo(readDBKotlinTopicsRepoImpl: ReadDBKotlinTopicsRepoImpl): ReadDBKotlinTopicsRepo

    @Binds
    fun bindReadDBCoroutineTopicsRepo(readDBCoroutineTopicsRepoImpl: ReadDBCoroutineTopicsRepoImpl): ReadDBCoroutineTopicsRepo

    @Binds
    fun bindReadDBTopicPointsRepo(readDBTopicPointsRepoImpl: ReadDBTopicPointsRepoImpl): ReadDBTopicPointsRepo

    @Binds
    fun bindWriteDBKotlinTopicsRepo(writeDBKotlinTopicsRepoImpl: WriteDBKotlinTopicsRepoImpl): WriteDBKotlinTopicsRepo

    @Binds
    fun bindWriteDBCoroutineTopicRepo(writeDBCoroutineTopicRepoImpl: WriteDBCoroutineTopicsRepoImpl): WriteDBCoroutineTopicsRepo

    @Binds
    fun bindWriteDBTopicPointsRepo(writeDBTopicPointsRepoImpl: WriteDBTopicPointsRepoImpl): WriteDBTopicPointsRepo

    @Binds
    fun bindVersionInfoRepo(versionInfoRepoImpl: VersionInfoRepoImpl): VersionInfoRepo

    @Named("git")
    @Binds
    fun bindGitCoursesRepoImpl(gitCoursesRepoImpl: GetGitCoursesRepoImpl): GetCoursesRepo

    @Named("db")
    @Binds
    fun bindDBCoursesRepoImpl(readDbCoursesRepoImpl: GetDBCoursesRepoImpl): GetCoursesRepo

    @Binds
    fun bindWriteDBCoursesRepoImpl(writeDBCoursesRepoImpl: WriteDBCoursesRepoImpl): WriteDBCoursesRepo
}