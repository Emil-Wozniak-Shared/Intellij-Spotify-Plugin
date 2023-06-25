package pl.ejdev.spotifyplugin.api.service.player

import arrow.core.Either
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithAuthorizedApi
import se.michaelthelin.spotify.model_objects.miscellaneous.Device

sealed class DevicePlayerAction

object UsersAvailableDevicesPlayAction : DevicePlayerAction()

fun devicesPlayerActions(action: DevicePlayerAction): Either<BaseError, Array<Device>> =
    when (action) {
        is UsersAvailableDevicesPlayAction -> tryWithAuthorizedApi(spotifyApi, "User available devices") {
            usersAvailableDevices.build().execute()
        }
    }
