package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.SpotifyApiService
import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import se.michaelthelin.spotify.model_objects.specification.*

@Service(PROJECT)
@State(name = "Playlists")
class UserPlaylistSpotifyService : PersistentStateComponent<Array<SimplifiedPlaylistModel>>, DumbAware {
    private val spotifyApiService: SpotifyApiService = SpotifyApiService()
    private var serviceState = arrayOf<SimplifiedPlaylistModel>()

    override fun getState(): Array<SimplifiedPlaylistModel> = serviceState

    override fun loadState(state: Array<SimplifiedPlaylistModel>) {
        getCurrentUserPlaylists()
            .onRight {
                val pages = it.items.map(SimplifiedPlaylistModel.Companion::from)
                serviceState = pages.toTypedArray()
                pages.forEach { state + it }
            }
    }

    private fun getCurrentUserPlaylists(): Either<BaseError, Paging<PlaylistSimplified>> =
        spotifyApiService.getCurrentUserPlaylists()

}
