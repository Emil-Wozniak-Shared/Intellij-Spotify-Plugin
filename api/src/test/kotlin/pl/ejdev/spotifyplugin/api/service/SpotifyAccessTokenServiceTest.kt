package pl.ejdev.spotifyplugin.api.service

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.string.shouldNotBeEmpty

class SpotifyAccessTokenServiceTest : FeatureSpec({
    feature("Spotify access token service ") {
        val subject = SpotifyAccessTokenService()

        scenario("returns access token from Spotify API") {
            val tokenService = subject.requestAccessToken()
            val accessToken = subject.getAccessToken()

            accessToken.shouldNotBeEmpty()
        }
    }
})
