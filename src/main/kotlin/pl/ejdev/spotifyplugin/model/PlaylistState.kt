package pl.ejdev.spotifyplugin.model

data class PlaylistState(
    // TODO we need more than that
    var id: String = "37i9dQZF1DWYNSm3Z3MxiM", // Classic Rock Workout
    var name: String = "",
    var description: String = "",
    var tracks: List<TrackDetails> = listOf()
)

data class TrackDetails(
    val name: String,
    val href: String
)
