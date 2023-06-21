package pl.ejdev.spotifyplugin.api.service.playlist

import arrow.core.Either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithAuthorizedApi
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified

private val logger = KotlinLogging.logger { }

fun fetchCurrentUserPlaylists(): Either<BaseError, Paging<PlaylistSimplified>> =
    tryWithAuthorizedApi(spotifyApi, "Get user playlists") {
        listOfCurrentUsersPlaylists.build().execute()
    }

fun fetchPlaylist(id: String): Either<BaseError, Playlist> = tryWithAuthorizedApi<Playlist>(
    spotifyApi,
    message = "Fetch playlist: $id",
    perform = {
        getPlaylist(id)
            .market(CountryCode.PL)
            .build()
            .execute()
            .also { logger.warn { "Playlist fetched ${it.name}" } }
    }
)

fun addTrackToQueue(id: String, href: String): Either<BaseError, SnapshotResult> =
    tryWithAuthorizedApi(spotifyApi,"add track to queue") {
        addItemsToPlaylist(id, arrayOf(href)).build().execute()
    }
