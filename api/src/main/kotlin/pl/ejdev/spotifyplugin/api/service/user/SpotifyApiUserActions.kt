package pl.ejdev.spotifyplugin.api.service.user

import arrow.core.Either
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.utils.tryWithApi
import se.michaelthelin.spotify.model_objects.specification.User

private val logger = KotlinLogging.logger { }

fun fetchCurrentUser(): Either<BaseError, User> = tryWithApi(
    message = "Get current user",
    perform = { currentUsersProfile.build().execute().also { logger.warn { "Current user: $it" } } }
)




