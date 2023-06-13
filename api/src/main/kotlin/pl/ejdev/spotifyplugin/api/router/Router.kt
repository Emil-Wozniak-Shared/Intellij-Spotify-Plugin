package pl.ejdev.spotifyplugin.api.router

import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.configuration.ROUTER_PORT
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

private val logger = KotlinLogging.logger { }

object Router {
    private val serverSocket = ServerSocket(ROUTER_PORT)
    private val clientSocket: Socket = serverSocket.accept()
    private val out = PrintWriter(clientSocket.getOutputStream(), true)

    fun listen(): Response<out Any> {
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val message: String = input.readLine()
        logger.warn { "Incoming message: $message" }
        return when {
            message.startsWith("GET /callback") ->
                message.split("callback?code=")[1].let(::CodeResponse)

            else -> EmptyResponse
        }
    }
}

sealed class Response<T>(val payload: T)

object EmptyResponse : Response<Unit>(Unit)

class CodeResponse(code: String) : Response<String>(code)