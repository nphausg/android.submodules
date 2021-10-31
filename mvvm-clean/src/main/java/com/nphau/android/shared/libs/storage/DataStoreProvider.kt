/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.libs.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface DataStoreProvider {
    val instance: DataStore<Preferences>
}