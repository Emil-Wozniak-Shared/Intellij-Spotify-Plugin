package pl.ejdev.spotifyplugin.errors

sealed class BaseError(val message: String)

class SpotifyApiError(message: String): BaseError(message)