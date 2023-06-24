package pl.ejdev.spotifyplugin.model

import se.michaelthelin.spotify.model_objects.IPlaylistItem
import se.michaelthelin.spotify.model_objects.specification.Playlist
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack

data class PlaylistState(
    // TODO we need more than that
    var id: String = "37i9dQZF1DWYNSm3Z3MxiM", // Classic Rock Workout
    var name: String = "",
    var description: String = "",
    var tracks: List<TrackDetails> = listOf()
) {
    fun from(playlist: Playlist) = apply {
        id = playlist.id
        name = playlist.name
        description = playlist.description
        tracks = playlist.tracks.items
            .take(10)
            .map(PlaylistTrack::getTrack)
            .map { item: IPlaylistItem -> TrackDetails(item.name, item.href) }
    }
}

data class TrackDetails(
    val name: String = "",
    val href: String = ""
)
