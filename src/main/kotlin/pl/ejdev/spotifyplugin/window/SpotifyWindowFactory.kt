package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import pl.ejdev.spotifyplugin.service.SpotifyService
import java.awt.Component
import javax.swing.JButton

private const val LOCKED = false
private const val NAME = "Spotify"
private const val PLAY = "Get playlist"
private const val STOP = "Refresh"

class SpotifyWindowFactory : ToolWindowFactory {
    private val contentFactory: ContentFactory = ContentFactory.getInstance()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        SpotifyWindow(toolWindow)
            .let { contentFactory.createContent(it.content, null, LOCKED) }
            .let(toolWindow.contentManager::addContent)
    }
}

class SpotifyWindow(private val toolWindow: ToolWindow) {
    private val service = toolWindow.project.service<SpotifyService>()
    private val label = JBLabel(NAME)
    private val playlistName = JBLabel("")
    private val playlistDescription = JBLabel("")
    private var trackPanel:JBPanel<JBPanel<*>>? = null

    val content: JBPanel<JBPanel<*>> = JBPanel<JBPanel<*>>().apply {
        add(label)
        add(JButton(PLAY).apply {
            addActionListener {
                this.text = if (service.isOn()) STOP else PLAY
                service.getPlaylist().map {
                    playlistName.text = it.name
                    playlistDescription.text = it.description
                    trackPanel = JBPanel<JBPanel<*>>()
                    it.tracks.items.forEach {
                        trackPanel?.add(JBLabel(it.track.name))
                    }
                }
            }
        })
        trackPanel?.let(::add)
        add(playlistName)
    }
}
