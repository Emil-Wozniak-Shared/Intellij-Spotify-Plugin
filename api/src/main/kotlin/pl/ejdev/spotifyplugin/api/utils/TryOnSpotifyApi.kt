package pl.ejdev.spotifyplugin.api.utils

import arrow.core.raise.either
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.errors.SpotifyApiError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.text.ParseException

private val logger = KotlinLogging.logger { }

internal inline fun <reified T> tryWithApi(
    message: String,
    perform: SpotifyApi.() -> T,
    onError: (Exception) -> BaseError = { SpotifyApiError("${it.message}") },
    conditionFailedMessage: String = "No access token",
    condition: () -> Boolean = { spotifyApi.accessToken != null }
) = either {
    logger.warn { message }
    try {
        if (condition()) {
            perform(spotifyApi)
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