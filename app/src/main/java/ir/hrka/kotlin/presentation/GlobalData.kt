package ir.hrka.kotlin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ir.hrka.kotlin.domain.entities.VersionsInfo
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

@Singleton
class GlobalData @Inject constructor() {

    private val _versionsInfo: MutableLiveData<VersionsInfo?> = MutableLiveData(null)
    val versionsInfo: LiveData<VersionsInfo?> = _versionsInfo


    @Throws(IllegalStateException::class)
    fun setVersionsInfo(versionsInfo: VersionsInfo) {
        if (_versionsInfo.value == null)
            _versionsInfo.value = versionsInfo
        else
            throw IllegalStateException("VersionsInfo is already set and cannot be modified.")
    }
}