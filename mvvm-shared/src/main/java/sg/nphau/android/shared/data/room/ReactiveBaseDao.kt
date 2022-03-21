/*
 * Created by nphau on 4/18/21 11:12 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 11:12 AM
 */

package sg.nphau.android.shared.data.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.rxjava3.core.Single

interface ReactiveBaseDao<T> {

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg obj: T): Single<List<Long>>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(obj: T): Single<Int>

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T): Single<Int>

}