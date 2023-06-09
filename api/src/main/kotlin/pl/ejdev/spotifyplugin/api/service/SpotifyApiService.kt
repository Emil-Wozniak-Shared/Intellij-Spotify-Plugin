package pl.ejdev.spotifyplugin.api.service

import arrow.core.Either
import arrow.core.raise.either
import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.api.errors.BaseError
import pl.ejdev.spotifyplugin.api.errors.SpotifyApiError
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Playlist

private val logger = KotlinLogging.logger {  }

class SpotifyApiService {
    private var spotifyApiInstance: SpotifyApi = SpotifyApi.Builder().build()
    private val accessTokenService: SpotifyAccessTokenService = SpotifyAccessTokenService()

    // Classic Rock Workout TODO we need more than that
    fun getPlaylist(id: String = "37i9dQZF1DWYNSm3Z3MxiM"): Either<BaseError, Playlist> =
        either<BaseError, Playlist> {
            try {
                spotifyApiInstance
                    .apply { accessToken = accessTokenService.requestAccessToken().getAccessToken() }
                    .getPlaylist(id)
                    .market(CountryCode.SE)
                    .build()
                    .execute()
            } catch (e: Exception) {
                raise(SpotifyApiError(e.message!!))
            }
        }
}