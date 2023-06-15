package pl.ejdev.spotifyplugin.api.service

import arrow.core.Either
import arrow.core.raise.either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.errors.SpotifyApiError
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.User
import java.io.IOException
import java.net.URI
import java.text.ParseException

private val logger = KotlinLogging.logger { }

class SpotifyApiService {
    private var code: String = ""

    fun authorizationCodeUri(): URI =
        spotifyApi.authorizationCodeUri()
            .scope("user-read-birthdate")
            .scope("user-read-email")
            .scope("playlist-read-private")
            .scope("playlist-read-collaborative")
            .build()
            .execute()

    fun authorizationCode() {
        try {
            if (code.isNotBlank()) {
                val authorizationCodeCredentials = spotifyApi.authorizationCode(code).build().execute()
                spotifyApi.accessToken = authorizationCodeCredentials.accessToken
                spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
                logger.warn("Expires in: " + authorizationCodeCredentials.expiresIn)
            } else {
                logger.warn { "code is blank " }
            }
        } catch (e: IOException) {
            logger.error("Error: " + e.message)
        } catch (e: SpotifyWebApiException) {
            logger.error("Error: " + e.message)
        } catch (e: ParseException) {
            logger.error("Error: " + e.message)
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
                } else {
                    logger.warn { "No access token" }
                    raise(SpotifyApiError("No access token"))
                }
            } catch (e: Exception) {
                logger.error { "${e.message}" }
                raise(SpotifyApiError(e.message!!))
            }
        }


    fun setCode(code: String) {
        this.code = code
        logger.warn { "Set client code: $code" }
    }

    fun addToQueue(id: String, href: String): Either<BaseError, SnapshotResult> =
        either<BaseError, SnapshotResult> {
            try {
                spotifyApi.addItemsToPlaylist(id, arrayOf(href)).build().execute()
            } catch (e: Exception) {
                raise(SpotifyApiError(e.message!!))
            }
        }

    fun getCurrentUser(): Either<BaseError, User> =
        either<BaseError, User> {
            try {
                logger.warn { "Get current user" }
                if (spotifyApi.accessToken != null) {
                    spotifyApi.currentUsersProfile.build().execute().also {
                        logger.warn { "Current user: $it" }
                    }
                } else {
                    logger.warn { "No access token" }
                    raise(SpotifyApiError("No access token"))
                }
            } catch (e: Exception) {
                logger.error { e.message }
                raise(SpotifyApiError(e.message!!))
            }
        }

    fun getCurrentUserPlaylists(): Either<BaseError, Paging<PlaylistSimplified>> = either {
        try {
            logger.warn { "Get user playlists" }
            if (spotifyApi.accessToken != null) {
                spotifyApi.listOfCurrentUsersPlaylists.build().execute()
                    .also { paging ->
                        logger.warn { paging }
                    }
            } else {
                logger.warn { "No access token" }
                raise(SpotifyApiError("No access token"))
            }
        } catch (e: Exception) {
            logger.error { e.message }
            raise(SpotifyApiError(e.message!!))
        }
    }
}