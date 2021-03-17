package com.yogenp.loginmvvm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences(
    context: Context
) {

    private val applicationContext = context.applicationContext

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }

    val authToken: Flow<String?>
        get() = applicationContext.dataStore.data.map {
            it[KEY_AUTH]
        }

    suspend fun saveAuthToken(authToken: String) {
        applicationContext.dataStore.edit {
            it[KEY_AUTH] = authToken
        }
    }

    suspend fun clear(){
        applicationContext.dataStore.edit {
            it.clear()
        }
    }

}