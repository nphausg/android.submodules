/*
 * Created by nphau on 21/03/2022, 00:30
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21/03/2022, 20:23
 */
package sg.nphau.android.shared.common

open class Singleton<out T : Any>(creator: () -> T) {

    private var creator: (() -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(): T {
        val i = instance
        if (i != null)
            return i
        return synchronized(this) {
            val i2 = instance
            if (i2 != null)
                i2
            else {
                val created = creator!!()
                instance = created
                creator = null
                created
            }
        }
    }
}
