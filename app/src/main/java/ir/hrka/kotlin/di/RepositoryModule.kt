package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.git.ReadGitChangelogRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.preference.ReadPreferencesRepoImpl
import ir.hrka.kotlin.data.repositories.preference.WritePreferencesRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBTopicsRepoImpl
import ir.hrka.kotlin.domain.repositories.read.ReadChangelogRepo
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import ir.hrka.kotlin.domain.repositories.read.ReadPreferencesRepo
import ir.hrka.kotlin.domain.repositories.write.WritePreferencesRepo
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindReadChangelogRepoImpl(readGitChangelogRepoImpl: ReadGitChangelogRepoImpl): ReadChangelogRepo

    @Binds
    fun bindReadPreferencesRepoImpl(readPreferencesRepoImpl: ReadPreferencesRepoImpl): ReadPreferencesRepo

    @Binds
    fun bindWritePreferencesRepoImpl(writePreferencesRepoImpl: WritePreferencesRepoImpl): WritePreferencesRepo

    @Named("git")
    @Binds
    fun bindGitCoursesRepoImpl(readGitCoursesRepoImpl: ReadGitCoursesRepoImpl): ReadCoursesRepo

    @Named("db")
    @Binds
    fun bindDBCoursesRepoImpl(readGitCoursesRepoImpl: ReadGitCoursesRepoImpl): ReadCoursesRepo

    @Binds
    fun bindWriteDBCoursesRepoImpl(writeDBCoursesRepoImpl: WriteDBCoursesRepoImpl): WriteCoursesRepo

    @Named("git")
    @Binds
    fun bindGitReadTopicsRepoImpl(readGitTopicsRepoImpl: ReadGitTopicsRepoImpl): ReadTopicsRepo

    @Named("db")
    @Binds
    fun bindDBReadTopicsRepoImpl(readGitTopicsRepoImpl: ReadGitTopicsRepoImpl): ReadTopicsRepo

    @Binds
    fun bindWriteTopicsRepoImpl(writeDBTopicsRepoImpl: WriteDBTopicsRepoImpl): WriteTopicsRepo
}