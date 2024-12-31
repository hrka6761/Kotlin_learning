package ir.hrka.kotlin.presentation

import android.util.Log
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.VersionType
import ir.hrka.kotlin.domain.entities.Version
import ir.hrka.kotlin.domain.entities.VersionsInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalData @Inject constructor() {

    private var _versionsInfo: VersionsInfo? = null
    private var _versionsList: List<Version>? = null
    private var _coursesVersionId = -1
    private var _kotlinVersionId = -1
    private var _coroutineVersionId = -1
    var lastVersionId: Int? = null
    var hasCoursesUpdate = false
    var hasKotlinTopicsUpdate = false
    var hasCoroutineTopicsUpdate = false
    var hasKotlinTopicsPointsUpdate = false
    var hasCoroutineTopicsPointsUpdate = false
    var updatedKotlinTopics: List<Int> = mutableListOf()
    var updatedCoroutineTopics: List<Int> = mutableListOf()


    fun initGlobalData(
        versionsInfo: VersionsInfo?,
        coursesVersionId: Int,
        kotlinVersionId: Int,
        coroutineVersionId: Int
    ) {
        _versionsInfo = versionsInfo
        lastVersionId = _versionsInfo?.lastVersionId
        _versionsList = _versionsInfo?.versions
        _coursesVersionId = coursesVersionId
        _kotlinVersionId = kotlinVersionId
        _coroutineVersionId = coroutineVersionId

        checkCoursesUpdate()
        checkKotlinTopicsUpdate()
        checkCoroutineTopicsUpdate()
        checkKotlinTopicsPointsUpdate()
        checkCoroutineTopicsPointsUpdate()
    }


    private fun checkCoursesUpdate() {
        if (_versionsInfo == null && _coursesVersionId > 0) return
        if (_coursesVersionId == lastVersionId) return

        if (_coursesVersionId == -1) {
            hasCoursesUpdate = true
            return
        }

        val nextVersionsList = _versionsList?.subList(_coursesVersionId, lastVersionId!!)


        if (!nextVersionsList.isNullOrEmpty()) {
            hasCoursesUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateCourses.name }
        }
    }

    private fun checkKotlinTopicsUpdate() {

    }

    private fun checkCoroutineTopicsUpdate() {

    }

    private fun checkKotlinTopicsPointsUpdate() {

    }

    private fun checkCoroutineTopicsPointsUpdate() {

    }
}