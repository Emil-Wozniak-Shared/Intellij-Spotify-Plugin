package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.spotifyplugin.service.PlayerSpotifyService
import pl.ejdev.spotifyplugin.service.PlaylistSpotifyService
import pl.ejdev.spotifyplugin.service.SpotifyAuthorizationService
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.section.authization.authorizationPanel
import pl.ejdev.spotifyplugin.window.components.section.playlist.playlistPanel

internal class SpotifyWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val spotifyAuthorizationService = toolWindow.project.service<SpotifyAuthorizationService>()
    private val userPlaylistSpotifyService = toolWindow.project.service<UserPlaylistSpotifyService>()
    private val playlistSpotifyService = toolWindow.project.service<PlaylistSpotifyService>()
    private val playerSpotifyService = toolWindow.project.service<PlayerSpotifyService>()

    private val url: String = spotifyAuthorizationService.authorizationCodeUri().toString()

    val content = panel {
        authorizationPanel(url, spotifyAuthorizationService, userPlaylistSpotifyService)
        playlistPanel(userPlaylistSpotifyService, playlistSpotifyService, playerSpotifyService)
    }

    override fun mayUseIndices(): Boolean = false
}
