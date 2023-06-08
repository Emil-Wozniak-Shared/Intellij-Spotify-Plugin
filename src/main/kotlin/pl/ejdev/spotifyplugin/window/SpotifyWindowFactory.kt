package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

private const val LOCKED = false

internal class SpotifyWindowFactory : ToolWindowFactory {
    private val contentFactory: ContentFactory = ContentFactory.getInstance()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        SpotifyWindow(toolWindow)
            .let { contentFactory.createContent(it.content, null, LOCKED) }
            .let(toolWindow.contentManager::addContent)
    }
}
