package pl.ejdev.spotifyplugin.api.utils

import arrow.core.raise.either
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.errors.SpotifyApiError
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.text.ParseException

private val logger = KotlinLogging.logger { }

internal inline fun <reified T> tryWithAuthorizedApi(
    api: SpotifyApi,
    message: String,
    perform: SpotifyApi.() -> T,
) = either {
    logger.warn { message }
    try {
        if (api.accessToken != null) {
            perform(api)
        } else {
            raise(SpotifyApiError("No access token"))
        }
    } catch (e: IOException) {
        logger.error("Error: " + e.message)
        raise(SpotifyApiError("${e.message}"))
    } catch (e: SpotifyWebApiException) {
        logger.error("Error: " + e.message)
        raise(SpotifyApiError("${e.message}"))
    } catch (e: ParseException) {
        logger.error("Error: " + e.message)
        raise(SpotifyApiError("${e.message}"))
    } catch (e: Exception) {
        logger.error { "${e.message}" }
        raise(SpotifyApiError("${e.message}"))
    }
}

internal inline fun <reified T> tryAuthorizeApi(
    api: SpotifyApi,
    message: String,
    perform: SpotifyApi.() -> T,
    conditionFailedMessage: String,
    condition: () -> Boolean,
    onError: (Exception) -> BaseError = { SpotifyApiError("${it.message}") },
) = either {
    logger.warn { message }
    try {
        if (condition()) {
            perform(api)
        } else {
            raise(SpotifyApiError(conditionFailedMessage))
        }
    } catch (e: IOException) {
        logger.error("Error: " + e.message)
        raise(onError(e))
    } catch (e: SpotifyWebApiException) {
        logger.error("Error: " + e.message)
        raise(onError(e))
    } catch (e: ParseException) {
        logger.error("Error: " + e.message)
        raise(onError(e))
    } catch (e: Exception) {
        logger.error { "${e.message}" }
        raise(onError(e))
    }
}
