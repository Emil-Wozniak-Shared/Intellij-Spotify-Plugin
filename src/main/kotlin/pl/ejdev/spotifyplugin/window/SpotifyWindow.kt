package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.spotifyplugin.model.PlaylistState
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.components.section.authization.authorizationPanel
import pl.ejdev.spotifyplugin.window.components.section.playlist.playlistPanel
import pl.ejdev.spotifyplugin.window.components.section.track.tracksPanel

internal class SpotifyWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val service: SpotifyService = toolWindow.project.service<SpotifyService>()
    private val playlist: PlaylistState = service.state
    private val url: String = service.authorizationCodeUri().toString()

    val content = panel {
        authorizationPanel(url, service)
        playlistPanel(playlist, service)
        tracksPanel(playlist, service)
    }

    override fun mayUseIndices(): Boolean = false
}
