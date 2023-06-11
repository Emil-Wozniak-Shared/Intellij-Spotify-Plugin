package pl.ejdev.spotifyplugin.model

data class PlaylistState(
    var id: String = "37i9dQZF1DWYNSm3Z3MxiM",
    var name: String = "",
    var description: String = "",
    var tracks: Map<String, String> = mapOf()
)
