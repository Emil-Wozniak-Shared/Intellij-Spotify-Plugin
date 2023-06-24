package pl.ejdev.spotifyplugin.api.service.authorization

internal val SCOPES = listOf(
//    Images
    "ugc-image-upload",
//    Spotify Connect
    "user-read-playback-state",
    "user-modify-playback-state",
    "user-read-currently-playing",
//    Playback
    "app-remote-control",
    "streaming",
//    Playlists
    "playlist-read-private",
    "playlist-read-collaborative",
    "playlist-modify-private",
    "playlist-modify-public",
//    Follow
    "user-follow-modify",
    "user-follow-read",
//    Listening History
    "user-read-playback-position",
    "user-top-read",
    "user-read-recently-played",
//    Library
    "user-library-modify",
    "user-library-read",
//    Users
    "user-read-email",
    "user-read-private",
)