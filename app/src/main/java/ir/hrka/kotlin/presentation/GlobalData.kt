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
    private var _lastVersionId: Int? = null
    private var _versionsList: List<Version>? = null
    private var _coursesVersionId = -1
    private var _kotlinVersionId = -1
    private var _coroutineVersionId = -1
    var _hasCoursesUpdate = false
    var _hasKotlinTopicsUpdate = false
    var _hasCoroutineTopicsUpdate = false
    var _hasKotlinTopicsPointsUpdate = false
    var _hasCoroutineTopicsPointsUpdate = false
    var _updatedKotlinTopics: List<Int> = mutableListOf()
    var _updatedCoroutineTopics: List<Int> = mutableListOf()


    fun initGlobalData(
        versionsInfo: VersionsInfo?,
        coursesVersionId: Int,
        kotlinVersionId: Int,
        coroutineVersionId: Int
    ) {
        _versionsInfo = versionsInfo
        _lastVersionId = _versionsInfo?.lastVersionId
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
        if (_versionsInfo == null) return
        if (_coursesVersionId == _lastVersionId) return

        if (_coursesVersionId == -1) {
            _hasCoursesUpdate = true
            return
        }

        val nextVersionsList = _versionsList?.subList(_coursesVersionId, _lastVersionId!!)


        if (!nextVersionsList.isNullOrEmpty()) {
            _hasCoursesUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateCourses.name }
        }
    }

    private fun checkKotlinTopicsUpdate() {
        if (_kotlinVersionId == _lastVersionId) {
            _hasKotlinTopicsUpdate = false
            return
        }

        val versionsList = _versionsInfo?.versions
        val currentVersionIndex = _kotlinVersionId!!
        val lastVersionIndex = _lastVersionId?.minus(1) ?: -1
        val newVersionsList =
            if (currentVersionIndex > 0 || lastVersionIndex > 0)
                versionsList?.subList(currentVersionIndex, lastVersionIndex)
            else
                null

        if (!newVersionsList.isNullOrEmpty()) {
            _hasKotlinTopicsUpdate =
                newVersionsList.any { version -> version.versionType == VersionType.UpdateKotlinTopics.name }
        }
    }

    private fun checkCoroutineTopicsUpdate() {
        if (_coroutineVersionId == _lastVersionId) {
            _hasCoroutineTopicsUpdate = false
            _hasCoroutineTopicsPointsUpdate = false
            return
        }

        val versionsList = _versionsInfo?.versions
        val currentVersionIndex = _coroutineVersionId!!
        val lastVersionIndex = _lastVersionId?.minus(1) ?: -1
        val newVersionsList =
            if (currentVersionIndex > 0 || lastVersionIndex > 0)
                versionsList?.subList(currentVersionIndex, lastVersionIndex)
            else
                null

        if (!newVersionsList.isNullOrEmpty()) {
            _hasCoroutineTopicsUpdate =
                newVersionsList.any { version -> version.versionType == VersionType.UpdateCoroutineTopics.name }
        }
    }

    private fun checkKotlinTopicsPointsUpdate() {
        if (_kotlinVersionId == _lastVersionId) {
            _hasKotlinTopicsPointsUpdate = false
            return
        }

        val versionsList = _versionsInfo?.versions
        val currentVersionIndex = _kotlinVersionId!!
        val lastVersionIndex = _lastVersionId?.minus(1) ?: -1
        val newVersionsList =
            if (currentVersionIndex > 0 || lastVersionIndex > 0)
                versionsList?.subList(currentVersionIndex, lastVersionIndex)
            else
                null

        if (!newVersionsList.isNullOrEmpty()) {
            var hasKotlinTopicsPointsUpdate = false
            var updatedKotlinTopics: MutableSet<Int>? = null

            newVersionsList.forEach { newVersion ->
                if (newVersion.versionType == VersionType.UpdateKotlinTopicPoints.name) {
                    hasKotlinTopicsPointsUpdate = true

                    if (updatedKotlinTopics == null)
                        updatedKotlinTopics = mutableSetOf()

                    newVersion.updatedKotlinTopics?.let { updatedKotlinTopics!!.addAll(it) }
                }
            }

            _hasKotlinTopicsPointsUpdate = hasKotlinTopicsPointsUpdate
            _updatedKotlinTopics = updatedKotlinTopics?.toList()
        }
    }

    private fun checkCoroutineTopicsPointsUpdate() {
        if (_coroutineVersionId == _lastVersionId) {
            _hasCoroutineTopicsPointsUpdate = false
            return
        }

        val versionsList = _versionsInfo?.versions
        val currentVersionIndex = _coroutineVersionId!!
        val lastVersionIndex = _lastVersionId?.minus(1) ?: -1
        val newVersionsList =
            if (currentVersionIndex > 0 || lastVersionIndex > 0)
                versionsList?.subList(currentVersionIndex, lastVersionIndex)
            else
                null

        if (!newVersionsList.isNullOrEmpty()) {
            var hasCoroutineTopicsPointsUpdate = false
            var updatedCoroutineTopics: MutableSet<Int>? = null

            newVersionsList.forEach { newVersion ->
                if (newVersion.versionType == VersionType.UpdateCoroutineTopicPoints.name) {
                    hasCoroutineTopicsPointsUpdate = true

                    if (updatedCoroutineTopics == null)
                        updatedCoroutineTopics = mutableSetOf()

                    newVersion.updatedKotlinTopics?.let { updatedCoroutineTopics!!.addAll(it) }
                }
            }

            _hasCoroutineTopicsPointsUpdate = hasCoroutineTopicsPointsUpdate
            _updatedCoroutineTopics = updatedCoroutineTopics?.toList()
        }
    }
}