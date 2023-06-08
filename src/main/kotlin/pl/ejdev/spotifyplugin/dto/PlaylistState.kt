package pl.ejdev.spotifyplugin.dto

data class PlaylistState(
    var name: String = "",
    var description: String = "",
    var tracks: Map<String, String> = mapOf()
)