package pl.ejdev.spotifyplugin.api.errors

sealed class BaseError(open val message: String)

data class SpotifyApiError(override val message: String): BaseError(message)

object NoAccessTokenError: BaseError("No access token")