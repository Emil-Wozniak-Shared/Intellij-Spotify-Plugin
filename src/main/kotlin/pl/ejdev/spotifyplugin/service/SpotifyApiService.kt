package pl.ejdev.spotifyplugin.service

import arrow.core.Either
import arrow.core.raise.either
import com.intellij.openapi.components.Service
import com.neovisionaries.i18n.CountryCode
import pl.ejdev.spotifyplugin.errors.BaseError
import pl.ejdev.spotifyplugin.errors.SpotifyApiError
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest

@Service
internal class SpotifyApiService {
    private val playlistID = "37i9dQZF1DWYNSm3Z3MxiM" // Classic Rock Workout TODO we need more than that
    private val spotifyToken: SpotifyAccessTokenService = SpotifyAccessTokenService()

    // For all requests an access token is needed
    var spotifyApi: SpotifyApi = SpotifyApi.Builder()
        .setAccessToken(spotifyToken.requestAccessToken().getAccessToken())
        .build()

    // Create a request object with the optional parameter "market"
    private val playlist: GetPlaylistRequest = spotifyApi.getPlaylist(playlistID)
        .market(CountryCode.SE)
        .build()

    fun getPlaylist(): Either<BaseError, Playlist> =
        either<BaseError, Playlist> {
            try {
                playlist.execute()
            } catch (e: Exception) {
                raise(SpotifyApiError(e.message!!))
            }
        }
}