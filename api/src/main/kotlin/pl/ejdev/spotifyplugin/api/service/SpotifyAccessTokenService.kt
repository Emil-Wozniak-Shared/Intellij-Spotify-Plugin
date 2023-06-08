package pl.ejdev.spotifyplugin.api.service

import com.google.gson.JsonParser
import pl.ejdev.spotifyplugin.api.configuration.*
import pl.ejdev.spotifyplugin.api.utils.connectAs
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


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