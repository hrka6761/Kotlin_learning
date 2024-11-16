package ir.hrka.kotlin.data.datasource

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: Editor,
) {

    suspend fun saveBoolean(preferenceKey: String, value: Boolean) {
        editor.putBoolean(preferenceKey, value)
        editor.apply()
    }

    suspend fun saveInteger(preferenceKey: String, value: Int) {
        editor.putInt(preferenceKey, value)
        editor.apply()
    }

    suspend fun saveString(preferenceKey: String, value: String) {
        editor.putString(preferenceKey, value)
        editor.apply()
    }


    suspend fun loadBoolean(preferenceKey: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(preferenceKey, defaultValue)

    suspend fun loadInteger(preferenceKey: String, defaultValue: Int): Int =
        sharedPreferences.getInt(preferenceKey, defaultValue)

    suspend fun loadString(preferenceKey: String, defaultValue: String?): String? =
        sharedPreferences.getString(preferenceKey, defaultValue)


    suspend fun clearAllSharedPreferences() {
        editor.clear()
        editor.apply()
    }

    suspend fun clearSharedPreferences(preferenceKey: String) {
        editor.remove(preferenceKey)
        editor.apply()
    }
}