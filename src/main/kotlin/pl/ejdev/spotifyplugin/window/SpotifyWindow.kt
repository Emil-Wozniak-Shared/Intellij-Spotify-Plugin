package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.spotifyplugin.extensions.listeners.defaultActions
import pl.ejdev.spotifyplugin.extensions.listeners.keyListener
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.configurations.GET_PLAYLIST
import java.awt.event.KeyEvent
import javax.swing.JLabel

internal class SpotifyWindow(private val toolWindow: ToolWindow): DumbUtil, DumbAware {
    private val service = toolWindow.project.service<SpotifyService>()
    private val playlist = service.state

    private lateinit var nameLabel: Cell<JLabel>
    private lateinit var descLabel: Cell<JLabel>
    private lateinit var label: Cell<JLabel>
    private lateinit var bindText: Cell<JBTextField>

    val content = panel {
        row {
            button(GET_PLAYLIST) {
                service.loadState(playlist)
                nameLabel.component.text = playlist.name
                descLabel.component.text = playlist.description
            }

            bindText = textField()
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
                                KeyEvent.VK_ENTER.toChar() -> {
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

    override fun mayUseIndices(): Boolean {
        return false;
    }
}
