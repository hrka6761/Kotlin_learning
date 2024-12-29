package ir.hrka.kotlin.presentation

import android.util.Log
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.VersionType
import ir.hrka.kotlin.domain.entities.VersionsInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalData @Inject constructor() {

    private var _versionsInfo: VersionsInfo? = null
    private var _lastVersionId: Int? = null
    var _kotlinVersionId: Int? = null
    var _coroutineVersionId: Int? = null
    var _hasKotlinTopicsUpdate: Boolean? = null
    var _hasCoroutineTopicsUpdate: Boolean? = null
    var _hasKotlinTopicsPointsUpdate: Boolean? = null
    var _hasCoroutineTopicsPointsUpdate: Boolean? = null
    var _updatedKotlinTopics: List<Int>? = null
    var _updatedCoroutineTopics: List<Int>? = null


    fun initGlobalData(
        versionsInfo: VersionsInfo,
        lastVersionId: Int,
        kotlinVersionId: Int,
        coroutineVersionId: Int
    ) {
        _versionsInfo = versionsInfo
        _lastVersionId = lastVersionId
        _kotlinVersionId = kotlinVersionId
        _coroutineVersionId = coroutineVersionId

        checkKotlinTopicsUpdate()
        checkCoroutineTopicsUpdate()
        checkKotlinTopicsPointsUpdate()
        checkCoroutineTopicsPointsUpdate()
    }

    private fun checkKotlinTopicsUpdate() {
        if (_kotlinVersionId == _lastVersionId) {
            _hasKotlinTopicsUpdate = false
            return
        }

        val versionsList = _versionsInfo?.versions
        val currentVersionIndex = _kotlinVersionId!!
        val lastVersionIndex = _lastVersionId?.minus(1)!!
        val newVersionsList = versionsList?.subList(currentVersionIndex, lastVersionIndex)

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
        val lastVersionIndex = _lastVersionId?.minus(1)!!
        val newVersionsList = versionsList?.subList(currentVersionIndex, lastVersionIndex)

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
        val lastVersionIndex = _lastVersionId?.minus(1)!!
        val newVersionsList = versionsList?.subList(currentVersionIndex, lastVersionIndex)

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
        val lastVersionIndex = _lastVersionId?.minus(1)!!
        val newVersionsList = versionsList?.subList(currentVersionIndex, lastVersionIndex)

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