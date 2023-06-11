package pl.ejdev.spotifyplugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import pl.ejdev.spotifyplugin.api.service.SpotifyAccessTokenService
import pl.ejdev.spotifyplugin.api.service.SpotifyApiService
import pl.ejdev.spotifyplugin.model.PlaylistState
import se.michaelthelin.spotify.model_objects.IPlaylistItem
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack

@Service(Service.Level.PROJECT)
@State(name = "Playlist")
class SpotifyService(
    private val project: Project
) : PersistentStateComponent<PlaylistState>, DumbAware {
    private val spotifyApiService: SpotifyApiService = SpotifyApiService(SpotifyAccessTokenService)
    private var playlistState: PlaylistState = PlaylistState()

    override fun getState(): PlaylistState = playlistState
    fun authorizationCodeUri() = spotifyApiService.authorizationCodeUri()

    fun authorizationCode() = spotifyApiService.authorizationCode()

    fun setCode(code: String) = spotifyApiService.setCode(code)

    override fun loadState(state: PlaylistState) {
        fetchPlaylist()
        state.apply {
            name = playlistState.name
            description = playlistState.description
            tracks = playlistState.tracks
        }
    }

    private fun fetchPlaylist() {
        spotifyApiService.getPlaylist(playlistState.id).map {
            playlistState.apply {
                name = it.name
                description = it.description
                tracks = it.tracks.items
                    .map(PlaylistTrack::getTrack)
                    .associate { item: IPlaylistItem -> item.name to item.href }
            }
        }
    }

    fun addToQueue(href: String) {
        spotifyApiService.addToQueue(playlistState.id, href)
    }
}
