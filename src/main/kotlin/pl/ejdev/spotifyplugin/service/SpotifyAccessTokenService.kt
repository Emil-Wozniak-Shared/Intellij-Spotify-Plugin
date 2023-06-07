package pl.ejdev.spotifyplugin.service

import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

private const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
private const val CONTENT_TYPE = "content-type"
private const val POST = "POST"

@Suppress("UNCHECKED_CAST")
class SpotifyAccessTokenService {
    private val payload = "grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}"
    private var response: Map<String, String> = mapOf()

    fun requestAccessToken() = apply {
        URL(TOKEN).connectAs<HttpURLConnection> {
            requestMethod = POST
            doOutput = true
            setRequestProperty(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
            outputStream.write(payload.toByteArray())
            inputStream.responseToMap().let { response = it }
            disconnect()
        }
    }

    fun getAccessToken() = response.getValue("access_token")

    private fun <T> URL.connectAs(actions: T.() -> Unit): T = (this.openConnection() as T).apply(actions)

    private fun InputStream.responseToMap(): Map<String, String> = this
        .let(::InputStreamReader)
        .let(::BufferedReader)
        .lines()
        .toList()
        .joinToString() // collect incoming data to JSON
        .let(JsonParser::parseString)
        .asJsonObject
        .entrySet()
        .associate { (k, v) -> k to v.asString } // transform json to association map

}