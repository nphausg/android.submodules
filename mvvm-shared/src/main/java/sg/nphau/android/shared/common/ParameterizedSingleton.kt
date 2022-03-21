/*
 * Created by nphau on 21/03/2022, 00:30
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21/03/2022, 20:23
 */
package sg.nphau.android.shared.common

open class ParameterizedSingleton<out T, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(argument: A): T {
        val checkInstance = instance
        if (checkInstance != null) return checkInstance

        return synchronized(this) {
            val synchronizedCheck = instance
            if (synchronizedCheck != null) return synchronizedCheck
            else {
                val createdInstance = creator!!(argument)
                instance = createdInstance
                creator = null
                createdInstance
            }
        }
    }
}
