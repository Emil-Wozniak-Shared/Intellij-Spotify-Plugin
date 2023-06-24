package pl.ejdev.spotifyplugin.api.service.player

import arrow.core.Either
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithAuthorizedApi

sealed class PlayerAction(val payload: String)

object PlayAction : PlayerAction("")
class AddItemToQueue(private val tractUri: String) : PlayerAction(tractUri)

class SetRepeatModeOnUsersPlayback(private val state: String = "track") : PlayerAction(state)


fun playerActions(action: PlayerAction): Either<BaseError, String> = when (action) {
    is PlayAction -> tryWithAuthorizedApi(spotifyApi, "Play music") {
        startResumeUsersPlayback().build().execute()
    }

    is AddItemToQueue -> tryWithAuthorizedApi(spotifyApi, "Add item to player queue") {
        addItemToUsersPlaybackQueue(action.payload).build().execute()
    }

    is SetRepeatModeOnUsersPlayback -> tryWithAuthorizedApi(spotifyApi, "Set repeat mode") {
        setRepeatModeOnUsersPlayback(action.payload).build().execute()
    }
}