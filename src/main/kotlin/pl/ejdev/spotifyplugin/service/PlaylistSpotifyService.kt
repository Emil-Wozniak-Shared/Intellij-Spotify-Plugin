package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.SpotifyApiService
import pl.ejdev.spotifyplugin.model.PlaylistState
import pl.ejdev.spotifyplugin.model.TrackDetails
import se.michaelthelin.spotify.model_objects.IPlaylistItem
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack

@Service(Service.Level.PROJECT)
@State(name = "Playlist")
class PlaylistSpotifyService : PersistentStateComponent<PlaylistState>, DumbAware {
    private val spotifyApiService: SpotifyApiService = SpotifyApiService()
    private var playlistState: PlaylistState = PlaylistState()

    override fun getState(): PlaylistState = playlistState

    override fun loadState(state: PlaylistState) {
        state.apply {
            id = playlistState.id
            name = playlistState.name
            description = playlistState.description
            tracks = playlistState.tracks
        }
    }

    fun fetchPlaylist(playlistId: String): Either<BaseError, PlaylistState> =
        spotifyApiService.getPlaylist(playlistId).map {
            playlistState.apply {
                id = playlistId
                name = it.name
                description = it.description
                tracks = it.tracks.items
                    .map(PlaylistTrack::getTrack)
                    .map { item: IPlaylistItem -> TrackDetails(item.name, item.href) }
            }
        }

    fun addToQueue(href: String): Either<BaseError, SnapshotResult> =
        spotifyApiService.addToQueue(playlistState.id, href)
}
