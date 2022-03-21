/*
 * Created by nphau on 01/11/2021, 00:47
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.server

import android.net.Uri
import android.os.Build
import sg.nphau.android.Launcher
import sg.nphau.android.shared.common.extensions.ioScope
import sg.nphau.android.shared.common.extensions.tag
import sg.nphau.android.shared.common.functional.tryOrNull
import sg.nphau.android.shared.libs.file.FileUtils
import sg.nphau.android.shared.libs.NetworkUtils
import sg.nphau.android.shared.libs.crypto.decodeBase64
import sg.nphau.android.shared.libs.crypto.toBase64
import sg.nphau.android.shared.libs.file.BitmapUtils
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import java.io.File
import sg.nphau.android.shared.libs.logger.logInfo
import io.ktor.server.cio.*
import sg.nphau.android.shared.data.entities.MediaType

/**
 * To run a Ktor server application, you need to create and configure a server first.
 * Server configuration can include different settings: a server engine, various engine-specific options,
 * host and port values, and so on
 * @see https://ktor.io/docs/engines.html
 * */
object LocalServer {

    private const val PORT = 5000
    private const val ERROR_MESSAGE = "Missing or malformed id"


    val server by lazy {
        embeddedServer(CIO, PORT, watchPaths = emptyList()) {
            install(WebSockets)
            install(CallLogging)
            routing {
                get("/") {
                    call.respondText("You are here in ${Build.MODEL}", ContentType.Text.Plain)
                }
                /**
                 * Files routing
                 * */
                get("/files/{contentUri}") {
                    try {
                        val file = resolveFile(call.parameters["contentUri"]!!)
                        if (file != null) {
                            call.response.header(
                                HttpHeaders.ContentDisposition,
                                ContentDisposition.Attachment.withParameter(
                                    ContentDisposition.Parameters.FileName,
                                    file.fileName()
                                ).toString()
                            )
                            call.respondFile(file)
                        } else {
                            return@get call.respondText(
                                text = ERROR_MESSAGE,
                                status = HttpStatusCode.NotFound
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return@get call.respondText(
                            text = ERROR_MESSAGE,
                            status = HttpStatusCode.BadRequest
                        )
                    }
                }
            }
        }
    }

    fun getCdnDomain() = "${NetworkUtils.getLocalIpAddress()}:$PORT"

    /**
     * Start Server
     * */
    fun start(server: ApplicationEngine) {
        ioScope {
            server.start(wait = true)
            logInfo(
                tag = LocalServer.tag(),
                message = "Server ${getCdnDomain()} was started."
            )
        }
    }

    /**
     * Stop Server
     * */
    fun stop(server: ApplicationEngine) {
        server.stop(1_000, 2_000)
        logInfo(
            tag = LocalServer.tag(),
            message = "Server ${getCdnDomain()} was stopped."
        )
    }

    fun encodeUri(uri: String, mediaType: String) =
        "$uri|$mediaType".toBase64()

    private fun decodeUri(encode: String) = tryOrNull {
        encode.decodeBase64().split("|").let {
            Pair(it[0], it[1])
        }
    } ?: Pair("", "")

    private fun resolveFile(parameters: String): File? {
        val (contentUri, type) = decodeUri(parameters)
        val uri = Uri.parse(contentUri)
        val path = FileUtils.getPhysicalPath(sg.nphau.android.Launcher.appContext, uri)
        if (path.isNullOrEmpty())
            return null
        var file = File(path)
        if (type == MediaType.IMAGE.name) {
            if (!BitmapUtils.isNormalOrientation(path)) {
                BitmapUtils
                    .createRotatedFile(sg.nphau.android.Launcher.appContext, path)
                    ?.let { file = it }
            }
        }
        return file
    }
}

fun File.fileName() = path.split("/").lastOrNull() ?: ""

fun CIOApplicationEngine.start() {
    LocalServer.start(this)
}

fun CIOApplicationEngine.stop() {
    LocalServer.stop(this)
}