package pl.ejdev.spotifyplugin.model

import se.michaelthelin.spotify.model_objects.miscellaneous.Device

data class DeviceModel(
    val id: String?,
    val active: Boolean,
    val privateSession: Boolean,
    val restricted: Boolean,
    val name: String,
    val type: String,
    val volumePercent: Int?,
) {
    companion object {
        fun from(device: Device) = device.run {
            DeviceModel(
                id = id,
                active = is_active,
                privateSession = is_private_session,
                restricted = is_restricted,
                name = name,
                type = type,
                volumePercent = volume_percent
            )
        }
    }
}
