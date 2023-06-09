package pl.ejdev.spotifyplugin.api.service

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import java.net.http.HttpClient

class SpotifyAccessTokenServiceTest : FeatureSpec({
    feature("Spotify access token service ") {
        val subject = SpotifyAccessTokenService()

        scenario("returns access token from Spotify API") {
            val response = subject.requestToken()

            response.statusCode() shouldBe 200
            response.uri().toString() shouldBe "https://accounts.spotify.com/api/token"
            with(response.body()) {
                access_token.shouldNotBeEmpty()
                token_type shouldBe "Bearer"
                expires_in shouldBe 3600
            }
            with(response.request()) {
                method() shouldBe "POST"
                headers().map() shouldBe mapOf("content-type" to listOf("application/x-www-form-urlencoded"))
            }
        }
    }
})
