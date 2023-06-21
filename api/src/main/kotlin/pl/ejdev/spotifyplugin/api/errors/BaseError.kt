package pl.ejdev.spotifyplugin.api.errors

sealed class BaseError(val message: String)

class SpotifyApiError(message: String): BaseError(message)

object NoAccessTokenError: BaseError("No access token")