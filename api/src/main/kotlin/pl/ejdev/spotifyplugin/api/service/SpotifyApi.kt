package pl.ejdev.spotifyplugin.api.service

import pl.ejdev.spotifyplugin.api.configuration.CLIENT_ID
import pl.ejdev.spotifyplugin.api.configuration.CLIENT_SECRET
import pl.ejdev.spotifyplugin.api.configuration.REDIRECT_URI
import se.michaelthelin.spotify.SpotifyApi
import java.net.URI

internal val spotifyApi = SpotifyApi.Builder()
    .setClientId(CLIENT_ID)
    .setClientSecret(CLIENT_SECRET)
    .setRedirectUri(REDIRECT_URI.let(::URI))
    .build()