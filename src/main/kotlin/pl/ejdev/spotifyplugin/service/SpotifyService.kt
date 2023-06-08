package pl.ejdev.spotifyplugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project
import pl.ejdev.spotifyplugin.api.service.SpotifyApiService
import pl.ejdev.spotifyplugin.model.PlaylistState
import se.michaelthelin.spotify.model_objects.IPlaylistItem
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack

@Service
@State(name = "Playlist")
class SpotifyService(
    private val project: Project
) : PersistentStateComponent<PlaylistState> {
    private val spotifyApiService: SpotifyApiService = SpotifyApiService()
    private var _state: PlaylistState = PlaylistState()

    override fun getState(): PlaylistState = _state

    override fun loadState(state: PlaylistState) {
        fetchPlaylist()
        state.apply {
            name = _state.name
            description = _state.description
            tracks = _state.tracks
        }
    }

    private fun fetchPlaylist() {
        spotifyApiService.getPlaylist().map {
            _state = PlaylistState(
                name = it.name,
                description = it.description,
                tracks = it.tracks.items
                    .map(PlaylistTrack::getTrack)
                    .associate { item: IPlaylistItem -> item.name to item.id }
            )
        }
    }
}

