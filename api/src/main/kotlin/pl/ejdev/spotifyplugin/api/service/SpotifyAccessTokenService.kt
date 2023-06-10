package pl.ejdev.spotifyplugin.api.service

import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.configuration.*
import pl.ejdev.spotifyplugin.api.model.AccessTok1enModel
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofByteArray
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler
import java.net.http.HttpResponse.BodySubscribers
import java.net.http.HttpResponse.BodySubscribers.ofInputStream

private const val PAYLOAD = "grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}"

object SpotifyAccessTokenService {
    private val logger = KotlinLogging.logger { }

    fun requestToken(): HttpResponse<AccessTok1enModel> =
        logger.warn ( "Request token" )
            .run { httpClient.send(tokenRequest, bodyHandler) }

    private val bodyHandler: BodyHandler<AccessTok1enModel> = BodyHandler<AccessTok1enModel> {
        BodySubscribers.mapping(ofInputStream(), objectMapper::readValue)
    }

    private val tokenRequest: HttpRequest =
        HttpRequest.newBuilder()
            .uri(URI.create(TOKEN_URI))
            .POST(ofByteArray(PAYLOAD.toByteArray()))
            .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
            .build()

}