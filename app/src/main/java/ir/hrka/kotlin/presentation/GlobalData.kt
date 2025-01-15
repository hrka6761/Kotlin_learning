package ir.hrka.kotlin.presentation

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
    var appVersionCode: Int? = null
        private set
    var lastVersionId: Int? = null
    var hasCoursesUpdate = false
    var hasKotlinTopicsUpdate = false
    var hasCoroutineTopicsUpdate = false
    var hasKotlinTopicsPointsUpdate = false
    var hasCoroutineTopicsPointsUpdate = false
    var updatedKotlinTopics: MutableSet<Int> = mutableSetOf()
    var updatedCoroutineTopics: MutableSet<Int> = mutableSetOf()


    fun initGlobalData(
        versionsInfo: VersionsInfo?,
        coursesVersionId: Int,
        kotlinVersionId: Int,
        coroutineVersionId: Int,
        appVersionCode: Int
    ) {
        _versionsInfo = versionsInfo
        this.appVersionCode = appVersionCode
        lastVersionId = _versionsInfo?.lastVersionId
        _versionsList = _versionsInfo?.versions
        _coursesVersionId = coursesVersionId
        _kotlinVersionId = kotlinVersionId
        _coroutineVersionId = coroutineVersionId

        checkCoursesUpdate()
        checkKotlinUpdate()
        checkCoroutineUpdate()
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

    private fun checkKotlinUpdate() {
        if (_versionsInfo == null && _kotlinVersionId > 0) return
        if (_kotlinVersionId == lastVersionId) return

        if (_kotlinVersionId == -1) {
            hasKotlinTopicsUpdate = true
            hasKotlinTopicsPointsUpdate = true
            return
        }

        val nextVersionsList = _versionsList?.subList(_kotlinVersionId, lastVersionId!!)

        if (!nextVersionsList.isNullOrEmpty()) {
            hasKotlinTopicsUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateKotlinTopics.name }
            hasKotlinTopicsPointsUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateKotlinTopicPoints.name }
        }

        if (hasKotlinTopicsPointsUpdate)
            nextVersionsList?.forEach { version ->
                if (version.versionType == VersionType.UpdateKotlinTopicPoints.name)
                    version.updatedKotlinTopics?.toList()?.let { updatedKotlinTopics.addAll(it) }
            }
    }

    private fun checkCoroutineUpdate() {
        if (_versionsInfo == null && _coroutineVersionId > 0) return
        if (_coroutineVersionId == lastVersionId) return

        if (_coroutineVersionId == -1) {
            hasCoroutineTopicsUpdate = true
            hasCoroutineTopicsPointsUpdate = true
            return
        }

        val nextVersionsList = _versionsList?.subList(_coroutineVersionId, lastVersionId!!)

        if (!nextVersionsList.isNullOrEmpty()) {
            hasCoroutineTopicsUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateCoroutineTopics.name }
            hasCoroutineTopicsPointsUpdate =
                nextVersionsList.any { version -> version.versionType == VersionType.UpdateCoroutineTopicPoints.name }
        }

        if (hasCoroutineTopicsPointsUpdate)
            nextVersionsList?.forEach { version ->
                if (version.versionType == VersionType.UpdateCoroutineTopicPoints.name)
                    version.updatedCoroutineTopics?.toList()?.let { updatedCoroutineTopics.addAll(it) }
            }
    }
}