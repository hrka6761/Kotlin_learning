package ir.hrka.kotlin.domain.repositories.git

import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.GitFileData

interface ReadGitTopicsRepo {

    suspend fun getTopicsList(course: Course): Resource<List<GitFileData>?>
}