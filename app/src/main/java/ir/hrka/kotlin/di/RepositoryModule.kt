package ir.hrka.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.hrka.kotlin.data.repositories.db.read.ReadDBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.db.read.ReadDBPointsRepoImpl
import ir.hrka.kotlin.data.repositories.db.read.ReadDBTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitChangelogRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitCoursesRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBTopicsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBPointsRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBSnippetCodesRepoImpl
import ir.hrka.kotlin.data.repositories.db.write.WriteDBSubPointsRepoImpl
import ir.hrka.kotlin.data.repositories.git.ReadGitPointsRepoImpl
import ir.hrka.kotlin.domain.repositories.read.ReadChangelogRepo
import ir.hrka.kotlin.domain.repositories.write.WriteCoursesRepo
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import ir.hrka.kotlin.domain.repositories.read.ReadPointsRepo
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import ir.hrka.kotlin.domain.repositories.write.WritePointsRepo
import ir.hrka.kotlin.domain.repositories.write.WriteSnippetCodesRepo
import ir.hrka.kotlin.domain.repositories.write.WriteSubPointsRepo
import ir.hrka.kotlin.domain.repositories.write.WriteTopicsRepo
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindReadChangelogRepoImpl(readGitChangelogRepoImpl: ReadGitChangelogRepoImpl): ReadChangelogRepo

    @Named("git")
    @Binds
    fun bindGitCoursesRepoImpl(readGitCoursesRepoImpl: ReadGitCoursesRepoImpl): ReadCoursesRepo

    @Named("db")
    @Binds
    fun bindDBCoursesRepoImpl(readDBCoursesRepoImpl: ReadDBCoursesRepoImpl): ReadCoursesRepo

    @Binds
    fun bindWriteDBCoursesRepoImpl(writeDBCoursesRepoImpl: WriteDBCoursesRepoImpl): WriteCoursesRepo

    @Named("git")
    @Binds
    fun bindGitReadTopicsRepoImpl(readGitTopicsRepoImpl: ReadGitTopicsRepoImpl): ReadTopicsRepo

    @Named("db")
    @Binds
    fun bindDBReadTopicsRepoImpl(readDBTopicsRepoImpl: ReadDBTopicsRepoImpl): ReadTopicsRepo

    @Binds
    fun bindWriteTopicsRepoImpl(writeDBTopicsRepoImpl: WriteDBTopicsRepoImpl): WriteTopicsRepo

    @Named("git")
    @Binds
    fun bindReadGitPointsRepoImpl(readGitPointsRepoImpl: ReadGitPointsRepoImpl): ReadPointsRepo

    @Named("db")
    @Binds
    fun bindReadDBPointsRepoImpl(readDBPointsRepoImpl: ReadDBPointsRepoImpl): ReadPointsRepo

    @Binds
    fun bindWritePointsRepoImpl(writeDBPointsRepoImpl: WriteDBPointsRepoImpl): WritePointsRepo

    @Binds
    fun bindWriteSubPointsRepoImpl(writeDBSubPointsRepoImpl: WriteDBSubPointsRepoImpl): WriteSubPointsRepo

    @Binds
    fun bindWriteSnippetCodesRepoImpl(writeDBSnippetCodesRepoImpl: WriteDBSnippetCodesRepoImpl): WriteSnippetCodesRepo
}