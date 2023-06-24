package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.*
import pl.ejdev.spotifyplugin.api.service.authorization.fetchAuthorizationCode
import pl.ejdev.spotifyplugin.api.service.authorization.getAuthorizationCodeUri
import pl.ejdev.spotifyplugin.api.service.authorization.setClientCode
import pl.ejdev.spotifyplugin.api.service.user.fetchCurrentUser
import pl.ejdev.spotifyplugin.model.UserState
import se.michaelthelin.spotify.model_objects.specification.User

@Service(PROJECT)
@State(name = AUTHORIZATION)
class SpotifyAuthorizationService : PersistentStateComponent<UserState>, DumbAware {

    private var userState: UserState = UserState()

    override fun getState(): UserState = userState
    override fun loadState(state: UserState) {
        getAuthorizationCode()
        getCurrentUser()
            .map(state::update)
            .map { userState = it }
    }

    fun setCode(code: String) = setClientCode(code)

    fun authorizationCodeUri() = getAuthorizationCodeUri()

    private fun getAuthorizationCode() = fetchAuthorizationCode()

    private fun getCurrentUser(): Either<BaseError, User> = fetchCurrentUser()

}