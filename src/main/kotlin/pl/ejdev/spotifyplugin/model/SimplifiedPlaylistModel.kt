package pl.ejdev.spotifyplugin.model

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified

data class SimplifiedPlaylistModel(
    var collaborative: Boolean? = null,
    var href: String? = null,
    var id: String? = null,
    var images: Array<ImageModel> = arrayOf(),
    var name: String? = null,
    var publicAccess: Boolean? = null,
    var snapshotId: String? = null,
    var tracks: PlaylistTracksInformation? = null,
    var type: ModelObjectTypeModel? = null,
    var uri: String? = null,
) {

    companion object {
        fun from(playlist: PlaylistSimplified) = playlist.run {
            SimplifiedPlaylistModel(
                collaborative = isCollaborative,
                href = href,
                id = id,
                images = images.map { ImageModel(it.height, it.url, it.width) }.toTypedArray(),
                name = name,
                publicAccess = isPublicAccess,
                snapshotId = snapshotId,
                tracks = tracks?.run { PlaylistTracksInformation(href, total) },
                type = type?.name?.let(ModelObjectTypeModel::valueOf),
                uri = uri
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimplifiedPlaylistModel

        if (collaborative != other.collaborative) return false
        if (href != other.href) return false
        if (id != other.id) return false
        if (!images.contentEquals(other.images)) return false
        if (name != other.name) return false
        if (publicAccess != other.publicAccess) return false
        if (snapshotId != other.snapshotId) return false
        if (tracks != other.tracks) return false
        if (type != other.type) return false
        return uri == other.uri
    }

    override fun hashCode(): Int {
        var result = collaborative?.hashCode() ?: 0
        result = 31 * result + (href?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + images.contentHashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (publicAccess?.hashCode() ?: 0)
        result = 31 * result + (snapshotId?.hashCode() ?: 0)
        result = 31 * result + (tracks?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (uri?.hashCode() ?: 0)
        return result
    }
}