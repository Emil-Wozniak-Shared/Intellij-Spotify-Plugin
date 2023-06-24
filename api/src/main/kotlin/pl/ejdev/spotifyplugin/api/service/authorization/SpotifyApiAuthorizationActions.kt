package pl.ejdev.spotifyplugin.api.service.authorization

import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryAuthorizeApi
import java.net.URI

private val logger = KotlinLogging.logger { }

private var clientCode: String = ""

fun setClientCode(code: String) {
    clientCode = code
    logger.warn { "Client code: $clientCode" }
}

fun getAuthorizationCodeUri(): URI =
    spotifyApi.authorizationCodeUri()
        .scope(SCOPES.joinToString(" "))
        .build()
        .execute()

fun fetchAuthorizationCode() {
    tryAuthorizeApi(
        spotifyApi,
        message = "Get authorization code",
        perform = {
            val authorizationCodeCredentials = spotifyApi.authorizationCode(clientCode).build().execute()
            spotifyApi.accessToken = authorizationCodeCredentials.accessToken
            spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
            logger.warn("Expires in: " + authorizationCodeCredentials.expiresIn)
        },
        condition = clientCode::isNotBlank,
        conditionFailedMessage = "Client code is blank"
    )
}