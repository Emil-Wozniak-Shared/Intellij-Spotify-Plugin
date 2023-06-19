package pl.ejdev.spotifyplugin.api.service.playlist

import arrow.core.Either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithApi
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import java.net.URI

private val logger = KotlinLogging.logger { }

fun fetchCurrentUserPlaylists(): Either<BaseError, Paging<PlaylistSimplified>> = tryWithApi(
    message = "Get user playlists",
    perform = { listOfCurrentUsersPlaylists.build().execute() }
)

fun fetchPlaylist(id: String): Either<BaseError, Playlist> = tryWithApi<Playlist>(
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
    tryWithApi(
        message = "add track to queue",
        perform = { addItemsToPlaylist(id, arrayOf(href)).build().execute() }
    )
