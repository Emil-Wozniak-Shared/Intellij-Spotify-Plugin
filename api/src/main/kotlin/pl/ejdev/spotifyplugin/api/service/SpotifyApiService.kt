package pl.ejdev.spotifyplugin.api.service

import arrow.core.Either
import arrow.core.raise.either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.configuration.CLIENT_ID
import pl.ejdev.spotifyplugin.api.configuration.CLIENT_SECRET
import pl.ejdev.spotifyplugin.api.configuration.REDIRECT_URI
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.errors.SpotifyApiError
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.Playlist
import java.io.IOException
import java.net.URI
import java.text.ParseException


private val logger = KotlinLogging.logger { }

class SpotifyApiService(
    private val accessTokenService: SpotifyAccessTokenService
) {

    private var code: String = ""

    private val spotifyApi = SpotifyApi.Builder()
        .setClientId(CLIENT_ID)
        .setClientSecret(CLIENT_SECRET)
        .setRedirectUri(REDIRECT_URI.let(::URI))
        .build()


    fun authorizationCodeUri(): URI =
        spotifyApi.authorizationCodeUri().build().execute()

    fun authorizationCode() {
        try {
            val authorizationCodeCredentials = spotifyApi.authorizationCode(code).build().execute()
            spotifyApi.accessToken = authorizationCodeCredentials.accessToken
            spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
            logger.warn("Expires in: " + authorizationCodeCredentials.expiresIn)
        } catch (e: IOException) {
            logger.warn("Error: " + e.message)
        } catch (e: SpotifyWebApiException) {
            logger.warn("Error: " + e.message)
        } catch (e: ParseException) {
            logger.warn("Error: " + e.message)
        }
    }


    fun getPlaylist(id: String): Either<BaseError, Playlist> =
        either<BaseError, Playlist> {
            logger.warn { "Fetch playlist: $id" }
            try {
                if (spotifyApi.accessToken != null) {
                    spotifyApi
                        .getPlaylist(id)
                        .market(CountryCode.PL)
                        .build()
                        .execute()
                        .also { logger.warn { "Playlist fetched ${it.name}" } }
                } else raise(SpotifyApiError("No access token"))
            } catch (e: Exception) {
                logger.error { "${e.message}" }
                raise(SpotifyApiError(e.message!!))
            }
        }


    fun setCode(code: String) {
        this.code = code
        logger.warn { "code: $code" }
    }

    fun addToQueue(id: String, href: String): Either<BaseError, SnapshotResult> =
        either<BaseError, SnapshotResult> {
            try {
                spotifyApi.addItemsToPlaylist(id, arrayOf(href)).build().execute()
            } catch (e: Exception) {
                raise(SpotifyApiError(e.message!!))
            }
        }
}