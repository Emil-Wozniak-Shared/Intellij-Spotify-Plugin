package pl.ejdev.spotifyplugin.api.service.playlist

import arrow.core.Either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithAuthorizedApi
import se.michaelthelin.spotify.model_objects.AbstractModelObject
import se.michaelthelin.spotify.model_objects.specification.Paging
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified

private val logger = KotlinLogging.logger { }

sealed class PlaylistAction
object CurrentUserPlaylistsAction : PlaylistAction()
class GetPlaylistAction(val id: String) : PlaylistAction()
class AddTrackToQueueAction(val id: String, val href: String) : PlaylistAction()

fun playlistAction(action: PlaylistAction): Either<BaseError, AbstractModelObject> =
    when (action) {
        is CurrentUserPlaylistsAction -> tryWithAuthorizedApi(spotifyApi, "Get user playlists") {
            listOfCurrentUsersPlaylists.build().execute()
        }

        is AddTrackToQueueAction -> tryWithAuthorizedApi(spotifyApi, "add track to queue") {
            addItemsToPlaylist(action.id, arrayOf(action.href)).build().execute()
        }

        is GetPlaylistAction -> tryWithAuthorizedApi<Playlist>(
            spotifyApi,
            message = "Fetch playlist: ${action.id}",
            perform = {
                getPlaylist(action.id)
                    .market(CountryCode.PL)
                    .build()
                    .execute()
                    .also { logger.warn { "Playlist fetched ${it.name}" } }
            }
        )
    }
