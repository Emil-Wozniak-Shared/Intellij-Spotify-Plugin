package pl.ejdev.spotifyplugin.model

enum class ModelObjectTypeModel(val type: String) {
    ALBUM("album"),
    ARTIST("artist"),
    AUDIO_FEATURES("audio_features"),
    EPISODE("episode"),
    GENRE("genre"),
    PLAYLIST("playlist"),
    SHOW("show"),
    TRACK("track"),
    USER("user");
}