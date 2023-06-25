package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.DumbAware
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.player.UsersAvailableDevicesPlayAction
import pl.ejdev.spotifyplugin.api.service.player.devicesPlayerActions
import pl.ejdev.spotifyplugin.model.DeviceModel
import se.michaelthelin.spotify.model_objects.miscellaneous.Device

private val logger = KotlinLogging.logger { }

@State(name = DEVICES)
@Service(Service.Level.PROJECT)
class DevicePlayerSpotifyService : PersistentStateComponent<Array<DeviceModel>>, DumbAware {
    private var devicesState: Array<DeviceModel> = arrayOf()

    override fun getState(): Array<DeviceModel> = devicesState

    override fun loadState(state: Array<DeviceModel>) {
        performAction()
            .map { devices -> devices.map(DeviceModel::from) }
            .map(List<DeviceModel>::toTypedArray)
            .map { devicesState = it }
        logger.warn { "Available devices: ${devicesState.size}" }
        devicesState.forEach {
            logger.warn { it }
        }
    }

    private fun performAction(): Either<BaseError, Array<Device>> =
        devicesPlayerActions(UsersAvailableDevicesPlayAction)
}
