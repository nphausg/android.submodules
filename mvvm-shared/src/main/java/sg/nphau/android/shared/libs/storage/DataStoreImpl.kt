/*
 * Created by nphau on 01/11/2021, 00:48
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs.storage

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import sg.nphau.android.shared.common.extensions.none
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreImpl constructor(val context: Context) : DataStoreProvider {

    override val instance: DataStore<Preferences> = context.dataStore

}

// region [Extension]
suspend fun DataStore<Preferences>.putBoolean(
    key: Preferences.Key<Boolean>, value: Boolean = false
) {
    edit { preferences ->
        preferences[key] = value
    }
}

suspend fun DataStore<Preferences>.putString(
    key: Preferences.Key<String>, value: String = ""
) {
    edit { preferences ->
        preferences[key] = value
    }
}

suspend fun DataStore<Preferences>.putLong(
    key: Preferences.Key<Long>, value: Long = 0L
) {
    edit { preferences ->
        preferences[key] = value
    }
}

fun DataStore<Preferences>.getBoolean(key: Preferences.Key<Boolean>) =
    data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { preferences -> preferences[key] ?: false }

fun DataStore<Preferences>.getLong(key: Preferences.Key<Long>) =
    data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { preferences -> preferences[key] ?: 0L }


fun DataStore<Preferences>.getString(key: Preferences.Key<String>) =
    data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { preferences -> preferences[key] ?: "" }


inline fun <reified T> DataStore<Preferences>.get(key: String) = when (T::class) {
    Boolean::class -> this.getBoolean(booleanPreferencesKey(key))
    Long::class -> this.getLong(longPreferencesKey(key))
    else -> this.getString(stringPreferencesKey(key))
} as Flow<T>

suspend fun <T> DataStore<Preferences>.put(key: String, value: T) {
    when (value) {
        is Long -> this.putLong(longPreferencesKey(key), value)
        is Boolean -> this.putBoolean(booleanPreferencesKey(key), value)
        is String -> this.putString(stringPreferencesKey(key), value)
        else -> none()
    }
}
// endregion