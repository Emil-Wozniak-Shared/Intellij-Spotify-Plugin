package pl.ejdev.spotifyplugin.model

import com.neovisionaries.i18n.CountryCode
import se.michaelthelin.spotify.model_objects.specification.User

data class UserState(
    var birthdate: String? = null,
    var country: CountryCode? = CountryCode.PL,
    var displayName: String = "",
    var email: String? = null,
    var followers: Followers? = null,
    var href: String = "",
    var id: String = "",
    var images: Array<ImageModel> = arrayOf(),
    var product: ProductType? = null,
    var type: ModelObjectTypeModel? = null,
    var uri: String? = "",
) {
    fun update(user: User) = apply {
        birthdate = user.birthdate
        country = user.country
        displayName = user.displayName
        email = user.email
        followers = user.followers?.let { Followers(it.href, it.total) }
        href = user.href
        id = user.id
        images = user.images.map { ImageModel(it.height, it.url, it.width) }.toTypedArray()
        product = user.product?.name?.let(ProductType::valueOf)
        type = user.type?.name?.let(ModelObjectTypeModel::valueOf)
        uri = user.uri
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserState

        if (birthdate != other.birthdate) return false
        if (country != other.country) return false
        if (displayName != other.displayName) return false
        if (email != other.email) return false
        if (followers != other.followers) return false
        if (href != other.href) return false
        if (id != other.id) return false
        if (!images.contentEquals(other.images)) return false
        if (product != other.product) return false
        if (type != other.type) return false
        return uri == other.uri
    }

    override fun hashCode(): Int {
        var result = birthdate?.hashCode() ?: 0
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + displayName.hashCode()
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (followers?.hashCode() ?: 0)
        result = 31 * result + href.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + images.contentHashCode()
        result = 31 * result + (product?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (uri?.hashCode() ?: 0)
        return result
    }

    data class Followers(
        var href: String? = null,
        var total: Int? = null
    )
}