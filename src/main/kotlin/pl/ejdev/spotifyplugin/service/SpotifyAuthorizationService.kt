package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.SpotifyApiService
import pl.ejdev.spotifyplugin.model.UserState
import se.michaelthelin.spotify.model_objects.specification.User

@Service(PROJECT)
@State(name = "Authorization")
class SpotifyAuthorizationService : PersistentStateComponent<UserState>, DumbAware {
    private val spotifyApiService: SpotifyApiService = SpotifyApiService()

    private var userState: UserState = UserState()

    override fun getState(): UserState = userState
    override fun loadState(state: UserState) {
        authorizationCode()
        getCurrentUser()
            .map(state::update)
            .map { userState = it }
    }

    fun setCode(code: String) = spotifyApiService.setCode(code)

    internal fun authorizationCodeUri() = spotifyApiService.authorizationCodeUri()

    private fun authorizationCode() = spotifyApiService.authorizationCode()

    private fun getCurrentUser(): Either<BaseError, User> = spotifyApiService.getCurrentUser()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpotifyAuthorizationService

        if (spotifyApiService != other.spotifyApiService) return false
        return userState == other.userState
    }

    override fun hashCode(): Int {
        var result = spotifyApiService.hashCode()
        result = 31 * result + userState.hashCode()
        return result
    }
}