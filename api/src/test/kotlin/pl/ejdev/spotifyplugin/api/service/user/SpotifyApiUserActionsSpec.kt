package pl.ejdev.spotifyplugin.api.service.user

import com.neovisionaries.i18n.CountryCode.PL
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import pl.ejdev.spotifyplugin.api.errors.NoAccessTokenError
import pl.ejdev.spotifyplugin.api.service.mockSpotifyApi
import se.michaelthelin.spotify.enums.ModelObjectType.USER
import se.michaelthelin.spotify.enums.ProductType.FREE
import se.michaelthelin.spotify.model_objects.specification.User
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest
import java.time.LocalDate
import java.util.*

private const val FAKER = "faker"
private val ID = UUID.randomUUID().toString()
private val BIRTH_DATE = LocalDate.now().toString()
private const val EMAIL = "foo@domain.pl"
private const val NAME = "FOO"
private const val HREF = "http://localhost:80/href"

class SpotifyApiUserActionsSpec : FeatureSpec({

    feature("Fetch current user") {

        scenario("returns user data from spotify api") {
            // given
            mockSpotifyApi<GetCurrentUsersProfileRequest, User>(responseBody = user()) {
                every { currentUsersProfile } returns GetCurrentUsersProfileRequest.Builder(FAKER)
            }
            // when
            val response = fetchCurrentUser()

            // then
            response.getOrNull().let(::requireNotNull).run {
                id shouldBe ID
                birthdate shouldBe BIRTH_DATE
                displayName shouldBe NAME
                country shouldBe PL
                email shouldBe EMAIL
                href shouldBe HREF
                product shouldBe FREE
                type shouldBe USER
            }
        }
    }
})

private fun user(): User = User.Builder()
    .setId(ID)
    .setBirthdate(BIRTH_DATE)
    .setCountry(PL)
    .setEmail(EMAIL)
    .setDisplayName(NAME)
    .setHref(HREF)
    .setProduct(FREE)
    .setType(USER)
    .build()
