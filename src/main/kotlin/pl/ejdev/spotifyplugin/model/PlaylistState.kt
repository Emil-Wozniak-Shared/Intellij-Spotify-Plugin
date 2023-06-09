package pl.ejdev.spotifyplugin.model

data class PlaylistState(
    var name: String = "",
    var description: String = "",
    var tracks: Map<String, String> = mapOf()
)
