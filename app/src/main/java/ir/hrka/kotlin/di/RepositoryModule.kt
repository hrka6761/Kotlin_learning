package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.db.ReadDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.git.ChangelogRepoImpl
import ir.hrka.kotlin.data.repositories.db.ReadDBTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBCoroutineTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBKotlinTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.WriteDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.git.GitCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionDataRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionLocalInfoRepoImpl
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.git.ChangelogRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.db.ReadDBTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.WriteDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.git.CoursesRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionLocalInfoRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: ChangelogRepoImpl): ChangelogRepo

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
    fun bindWriteDBCoroutineTopicRepo(writeDBCoroutineTopicRepoImpl: WriteDBCoroutineTopicsRepoImpl): WriteDBCoroutineTopicsRepo

    @Binds
    fun bindWriteDBTopicPointsRepo(writeDBTopicPointsRepoImpl: WriteDBTopicPointsRepoImpl): WriteDBTopicPointsRepo

    @Binds
    fun bindVersionLocalInfoRepo(versionLocalInfoRepoImpl: VersionLocalInfoRepoImpl): VersionLocalInfoRepo

    @Named("git")
    @Binds
    fun bindGitCoursesRepoImpl(gitCoursesRepoImpl: GitCoursesRepoImpl): CoursesRepo
}