package ir.hrka.kotlin.core.utilities

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")


    suspend fun <T> saveData(preferencesKey: Preferences.Key<T>, value: T) =
        context.dataStore.edit { preferences -> preferences[preferencesKey] = value }

    fun <T> readData(preferencesKey: Preferences.Key<T>): Flow<T?> =
        context.dataStore.data.map { preferences -> preferences[preferencesKey] }
}