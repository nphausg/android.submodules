/*
 * Created by nphau on 01/11/2021, 00:49
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:30
 */

package com.nphau.android.shared.data.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun jacksonXmlMapper(builder: JacksonXmlModule.() -> Unit = {}): ObjectMapper =
    XmlMapper(JacksonXmlModule().apply {
        setDefaultUseWrapper(false)
        apply(builder)
    }).registerKotlinModule()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

inline fun <reified T : Any> parseAs(xml: String): T {
    return jacksonXmlMapper().readValue(xml, T::class.java)
}