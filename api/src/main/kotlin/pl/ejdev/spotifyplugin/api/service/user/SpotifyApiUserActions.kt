package pl.ejdev.spotifyplugin.api.service.user

import arrow.core.Either
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.service.spotifyApi
import pl.ejdev.spotifyplugin.api.utils.tryWithAuthorizedApi
import se.michaelthelin.spotify.model_objects.specification.User

private val logger = KotlinLogging.logger { }

fun fetchCurrentUser(): Either<BaseError, User> =
    tryWithAuthorizedApi(spotifyApi, "Get current user") {
        currentUsersProfile.build().execute()
    }.also { logger.warn { "Current user: $it" } }




