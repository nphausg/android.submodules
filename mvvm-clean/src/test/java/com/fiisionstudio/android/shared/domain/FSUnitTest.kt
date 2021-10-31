/*
 * Created by FS on 4/5/21 2:47 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/5/21 2:47 PM
 */

package com.nphau.android.shared.domain

import com.nphau.android.shared.libs.encryption.EncryptionUtils
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FSUnitTest {

    @Test
    fun generateKey() {
        val encrypt =
            EncryptionUtils.encryptAES("com.fiisionstudio.rokuremote", "1234567812345678") ?: ""
        println("encrypt: $encrypt")
        val decrypt = EncryptionUtils.decryptAES(encrypt, "1234567812345678")
        println("generateKey: $decrypt")
    }

}