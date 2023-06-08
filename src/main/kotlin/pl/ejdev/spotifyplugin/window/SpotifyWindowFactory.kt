package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBTextField
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import org.jetbrains.annotations.ApiStatus
import pl.ejdev.spotifyplugin.listeners.defaultActions
import pl.ejdev.spotifyplugin.listeners.keyListener
import pl.ejdev.spotifyplugin.service.SpotifyService
import java.awt.event.KeyEvent.VK_ENTER
import javax.swing.JLabel

private const val LOCKED = false
private const val GET_PLAYLIST = "Get playlist"

internal class SpotifyWindowFactory : ToolWindowFactory {
    private val contentFactory: ContentFactory = ContentFactory.getInstance()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        SpotifyWindow(toolWindow)
            .let { contentFactory.createContent(it.content, null, LOCKED) }
            .let(toolWindow.contentManager::addContent)
    }
}

internal class SpotifyWindow(private val toolWindow: ToolWindow) {
    private val service = toolWindow.project.service<SpotifyService>()
    private val playlist = service.state
    private lateinit var nameLabel: Cell<JLabel>
    private lateinit var descLabel: Cell<JLabel>
    private lateinit var label: Cell<JLabel>
    private lateinit var bindText: Cell<JBTextField>
    private val model = Model()

    val content = panel {
        row {
            button(GET_PLAYLIST) {
                service.loadState(playlist)
                nameLabel.component.text = playlist.name
                descLabel.component.text = playlist.description
            }

            bindText = textField()
                .bindText(model::textField)
                .apply {
                    component.keyListener(
                        typed = {
                            label.component.text += it.keyChar.toString()
                        },
                        pressed = {
                            it
                        },
                        released = {
                            label.component.text = it.defaultActions(label.component.text)
                            when (it.keyChar) {
                                VK_ENTER.toChar() -> {
                                    service.loadState(playlist)
                                    nameLabel.component.text = playlist.name
                                    descLabel.component.text = playlist.description
                                }
                            }
                        }
                    )
                }
        }
        indent {
            row { nameLabel = label(playlist.name) }
            row { descLabel = label(playlist.description) }
            row { label = label("") }
        }

        panel {
            group("Tracks:") {
                twoColumnsRow(
                    { rowComment("Type") },
                    { rowComment("Name") },
                )
                indent {
                    playlist.tracks.forEach { (name, id) ->
                        row {
                            label(id).gap(RightGap.COLUMNS)
                            label(name)
                        }
                    }
                }
            }
        }
    }
}

@ApiStatus.Internal
internal data class Model(
    var checkbox: Boolean = false,
    var textField: String = "",
    var intTextField: Int = 0,
    var comboBoxColor: Color = Color.GREY,
    var slider: Int = 0,
    var spinner: Int = 0,
    var radioButtonColor: Color = Color.GREY,
)

@ApiStatus.Internal
internal enum class Color {
    WHITE,
    GREY,
    BLACK
}