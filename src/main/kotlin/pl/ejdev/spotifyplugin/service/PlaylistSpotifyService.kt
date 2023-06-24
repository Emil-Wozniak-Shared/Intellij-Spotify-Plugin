package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.playlist.AddTrackToQueueAction
import pl.ejdev.spotifyplugin.api.service.playlist.GetPlaylistAction
import pl.ejdev.spotifyplugin.api.service.playlist.playlistAction
import pl.ejdev.spotifyplugin.model.PlaylistState
import se.michaelthelin.spotify.model_objects.special.SnapshotResult
import se.michaelthelin.spotify.model_objects.specification.Playlist

@Service(Service.Level.PROJECT)
@State(name = PLAYLIST)
class PlaylistSpotifyService : PersistentStateComponent<PlaylistState>, DumbAware {
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

    fun getPlaylist(playlistId: String): Either<BaseError, PlaylistState> =
        playlistAction(GetPlaylistAction(playlistId))
            .map { it as Playlist }
            .map(playlistState::from)

    fun addToQueue(href: String): Either<BaseError, SnapshotResult> =
        playlistAction(AddTrackToQueueAction(playlistState.id, href))
            .map { it as SnapshotResult }
}
