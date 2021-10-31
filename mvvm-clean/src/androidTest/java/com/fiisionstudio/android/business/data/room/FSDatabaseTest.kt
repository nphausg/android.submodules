/*
 * Created by FS on 4/18/21 9:43 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 4/18/21 9:43 AM
 */

package com.nphau.android.business.data.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nphau.android.kits.data.room.dao.AdConfigRecordDao
import com.nphau.android.kits.domain.entities.records.AdConfigRecord
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FSDatabaseTest {

    private lateinit var db: com.nphau.android.kits.data.room.FiisionDB
    private lateinit var adConfigRecordDao: com.nphau.android.kits.data.room.dao.AdConfigRecordDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, com.nphau.android.kits.data.room.FiisionDB::class.java).build()
        adConfigRecordDao = db.adConfigRecordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndRead() = runBlocking {
        val adConfigRecord =
            com.nphau.android.kits.domain.entities.records.AdConfigRecord(primaryId = 1)
        adConfigRecordDao.insertAll(adConfigRecord)
        val savedRecord = adConfigRecordDao.findById(adConfigRecord.primaryId)
        assertThat(savedRecord, equalTo(adConfigRecord))
    }
}