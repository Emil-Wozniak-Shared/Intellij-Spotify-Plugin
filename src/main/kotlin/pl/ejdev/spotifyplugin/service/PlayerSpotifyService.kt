package pl.ejdev.spotifyplugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.service.player.PlayerAction
import pl.ejdev.spotifyplugin.api.service.player.basePlayerActions

@State(name = PLAYER)
@Service(Service.Level.PROJECT)
class PlayerSpotifyService : PersistentStateComponent<PlayerSpotifyService.PlayerState>, DumbAware {
    private var playerState: PlayerState = PlayerState("")

    override fun getState(): PlayerState = playerState

    override fun loadState(state: PlayerState) {
        state.value = playerState.value
    }

    fun performAction(action: PlayerAction) {
        basePlayerActions(action).onRight {
            playerState.value = it
        }
    }

    data class PlayerState(
        var value: String
    )
}
