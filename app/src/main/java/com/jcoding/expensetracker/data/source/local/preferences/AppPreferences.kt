package com.jcoding.expensetracker.data.source.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class AppPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private fun <T> fetchPreferences(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.catch {
            if (this is IOException) {
                emit(emptyPreferences())
            }
        }.map { preferences ->
            preferences[key]
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////

    suspend fun saveCurrencyId(id: String) {
        dataStore.edit { preferences ->
            preferences[KEY_CURRENCY_ID] = id
        }
    }

    fun fetchCurrencyId(): Flow<String?> {
        return fetchPreferences(KEY_CURRENCY_ID)
    }


    companion object {
        private val KEY_CURRENCY_ID = stringPreferencesKey("currency_id")
    }
}
