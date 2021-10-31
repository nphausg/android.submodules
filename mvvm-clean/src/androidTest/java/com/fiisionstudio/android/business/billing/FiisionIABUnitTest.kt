/*
 * Created by FS on 5/7/21 9:35 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 5/7/21 9:35 PM
 */

package com.nphau.android.business.billing

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class FiisionIABUnitTest {

    @Test
    fun verifyIAB() {
        val signedData =
            "{\"orderId\":\"GPA.3302-9025-9379-19895\",\"packageName\":\"com.fiisionstudio.rokuremote\",\"productId\":\"com.fiisionstudio.rokuremote.monthlynontrial\",\"purchaseTime\":1620397564483,\"purchaseState\":0,\"purchaseToken\":\"pgldkhijiaikkaajafdgoinp.AO-J1OwTUqe9DXx-Jcfs8G6ovUluhqelB-j5NT8wLp1XzJWLGOUDu0lceZ7DzlJcYs9aOk7ylewn7ERRogt5J5pe0XbnqdFTAJLeljzXGn582C1x7ks3fhg\",\"autoRenewing\":true,\"acknowledged\":false}"
        val signature =
            "o6wlpuZ3FvaKbp708sbIBkofrBiJU4+qbfiCEhLjAIT/2u5JYi/j6773Zob+C06Yh27UPHcqt3w06wua1XPKGHhG3IYQOMJquQYw005xBIyFk3EPDaigInhJSbiJ+H5s5/F5lHaDm2+4jhXeGj31j+CATqwZrOz7ksXZHQA0V399TYreIY/I8mhLF2EJC7JkyCH31gm7zR5FvkBULik6GK/SAFu20AilSsoBz8Kb4KhxvDiB1yRpeekbw2guEzkjCj9paTpBASEDDapEP0SRTdPS76gCef+5y95mutWR/voauU9h01yoG71IK9Ob7Tf1BpYtumqPVbLsc9wpPrAcaQ=="
        assertTrue(
            com.nphau.android.kits.billing.BillingSecurity.verifyPurchase(
                com.nphau.android.kits.billing.BillingSecurity.BASE_64_ENCODED_PUBLIC_KEY,
                signedData,
                signature
            )
        )
    }
}