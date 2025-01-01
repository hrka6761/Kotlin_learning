package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.db.read.ReadDBCoroutineTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.read.ReadDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.ReadChangelogRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBCoroutineTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.WriteCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.WriteKotlinTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.DBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.DBReadTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.GitCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.GitReadTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.git.GitTopicPointsRepoImpl
import ir.hrka.kotlin.data.repositories.preference.VersionDataRepoImpl
import ir.hrka.kotlin.data.repositories.ReadPreferencesRepoImpl
import ir.hrka.kotlin.data.repositories.WritePreferencesRepoImpl
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.db.read.ReadDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.ReadChangelogRepo
import ir.hrka.kotlin.domain.repositories.WriteKotlinTopicsRepo
import ir.hrka.kotlin.domain.repositories.preference.VersionDataRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBCoroutineTopicsRepo
import ir.hrka.kotlin.domain.repositories.WriteCoursesRepo
import ir.hrka.kotlin.domain.repositories.db.write.WriteDBTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.ReadCoursesRepo
import ir.hrka.kotlin.domain.repositories.ReadTopicsRepo
import ir.hrka.kotlin.domain.repositories.git.ReadGitTopicPointsRepo
import ir.hrka.kotlin.domain.repositories.ReadPreferencesRepo
import ir.hrka.kotlin.domain.repositories.WritePreferencesRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAppInfoRepo(appInfoRepoImpl: ReadChangelogRepoImpl): ReadChangelogRepo

    @Binds
    fun bindReadGitTopicPointsRepo(gitTopicPointsRepoImpl: GitTopicPointsRepoImpl): ReadGitTopicPointsRepo

    @Binds
    fun bindVersionDataRepo(versionDataRepoImpl: VersionDataRepoImpl): VersionDataRepo

    @Binds
    fun bindReadDBCoroutineTopicsRepo(readDBCoroutineTopicsRepoImpl: ReadDBCoroutineTopicsRepoImpl): ReadDBCoroutineTopicsRepo

    @Binds
    fun bindReadDBTopicPointsRepo(readDBTopicPointsRepoImpl: ReadDBTopicPointsRepoImpl): ReadDBTopicPointsRepo

    @Binds
    fun bindWriteDBKotlinTopicsRepo(writeDBKotlinTopicsRepoImpl: WriteKotlinTopicsRepoImpl): WriteKotlinTopicsRepo

    @Binds
    fun bindWriteDBCoroutineTopicRepo(writeDBCoroutineTopicRepoImpl: WriteDBCoroutineTopicsRepoImpl): WriteDBCoroutineTopicsRepo

    @Binds
    fun bindWriteDBTopicPointsRepo(writeDBTopicPointsRepoImpl: WriteDBTopicPointsRepoImpl): WriteDBTopicPointsRepo

    @Binds
    fun bindReadPreferencesRepoImplRepo(readPreferencesRepoImpl: ReadPreferencesRepoImpl): ReadPreferencesRepo

    @Binds
    fun bindWritePreferencesRepoImplRepo(writePreferencesRepoImpl: WritePreferencesRepoImpl): WritePreferencesRepo

    @Named("git")
    @Binds
    fun bindGitCoursesRepoImpl(gitCoursesRepoImpl: GitCoursesRepoImpl): ReadCoursesRepo

    @Named("db")
    @Binds
    fun bindDBCoursesRepoImpl(readDbCoursesRepoImpl: DBCoursesRepoImpl): ReadCoursesRepo

    @Binds
    fun bindWriteDBCoursesRepoImpl(writeDBCoursesRepoImpl: WriteCoursesRepoImpl): WriteCoursesRepo

    @Named("git")
    @Binds
    fun bindGitTopicsRepoImpl(gitTopicsRepoImpl: GitReadTopicsRepoImpl): ReadTopicsRepo

    @Named("db")
    @Binds
    fun bindDBTopicsRepoImpl(dbTopicsRepoImpl: DBReadTopicsRepoImpl): ReadTopicsRepo
}