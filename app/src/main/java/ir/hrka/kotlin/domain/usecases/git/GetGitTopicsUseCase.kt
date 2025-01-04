package ir.hrka.kotlin.domain.usecases.git

import android.util.Log
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.domain.repositories.read.ReadTopicsRepo
import javax.inject.Inject
import javax.inject.Named

class GetGitTopicsUseCase @Inject constructor(@Named("git") private val readTopicsRepo: ReadTopicsRepo) {

    suspend operator fun invoke(course: Course) = readTopicsRepo.getTopics(course)
}