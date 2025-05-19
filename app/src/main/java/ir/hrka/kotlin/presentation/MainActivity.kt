package ir.hrka.kotlin.presentation

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent(appVersionCode = getAppVersionCode(this))
        }
    }


    private fun getAppVersionCode(context: Context): Int {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode = packageInfo.longVersionCode.toInt()

        return versionCode
    }
}