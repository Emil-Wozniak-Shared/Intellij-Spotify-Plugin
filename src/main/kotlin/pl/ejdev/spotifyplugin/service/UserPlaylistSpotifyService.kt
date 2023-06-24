package pl.ejdev.spotifyplugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.service.playlist.CurrentUserPlaylistsAction
import pl.ejdev.spotifyplugin.api.service.playlist.playlistAction
import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import se.michaelthelin.spotify.model_objects.specification.*

@Service(PROJECT)
@State(name = USER_PLAYLIST)
class UserPlaylistSpotifyService : PersistentStateComponent<Array<SimplifiedPlaylistModel>>, DumbAware {
    private var serviceState = arrayOf<SimplifiedPlaylistModel>()

    override fun getState(): Array<SimplifiedPlaylistModel> = serviceState

    override fun loadState(state: Array<SimplifiedPlaylistModel>) {
        playlistAction(CurrentUserPlaylistsAction)
            .map { it as Paging<*> }
            .onRight { paging ->
                val pages = paging.items
                    .filterIsInstance<PlaylistSimplified>()
                    .map(SimplifiedPlaylistModel::from)
                serviceState = pages.toTypedArray()
                pages.forEach { state + it }
            }
    }
}
