package pl.ejdev.spotifyplugin.api.service

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.data.AbstractDataRequest

internal inline fun <reified REQUEST : AbstractDataRequest<BODY>, reified BODY> mockSpotifyApi(
    omitToken: Boolean = false,
    responseBody: BODY,
    apiCallMocks: SpotifyApi.() -> Unit
) {
    mockkStatic("pl.ejdev.spotifyplugin.api.service.SpotifyApiKt")
    mockkConstructor(REQUEST::class)
    if (!omitToken) {
        every { spotifyApi.accessToken } returns "FAKER"
    }
    apiCallMocks(spotifyApi)
    every { anyConstructed<REQUEST>().execute() } returns responseBody
}