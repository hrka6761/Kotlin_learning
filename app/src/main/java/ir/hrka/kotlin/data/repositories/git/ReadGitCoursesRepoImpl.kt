package ir.hrka.kotlin.data.repositories.git

import ir.hrka.kotlin.core.errors.Errors
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.core.utilities.handleRemoteResponse
import ir.hrka.kotlin.data.datasource.git.GitAPI
import ir.hrka.kotlin.domain.entities.git.CoursesData
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.repositories.read.ReadCoursesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadGitCoursesRepoImpl @Inject constructor(
    private val gitAPI: GitAPI
) : ReadCoursesRepo {

    override suspend fun getCourses(): Flow<Result<List<Course>?, Errors>> =
        handleRemoteResponse(CoursesData::class.java) { gitAPI.getCourses() }
}