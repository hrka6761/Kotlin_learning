package ir.hrka.download

import android.content.Context
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadManager @Inject constructor(
    private val workManager: WorkManager,
    @ApplicationContext private val context: Context
) {

}