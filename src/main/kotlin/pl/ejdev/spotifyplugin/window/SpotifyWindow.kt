package pl.ejdev.spotifyplugin.window

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.JBColor
import com.intellij.ui.components.BrowserLink
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBFont.h1
import com.intellij.util.ui.JBFont.h4
import pl.ejdev.spotifyplugin.extensions.listeners.defaultActions
import pl.ejdev.spotifyplugin.extensions.listeners.keyListener
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.configurations.GET_PLAYLIST
import pl.ejdev.spotifyplugin.window.listeners.PasteDocumentListener
import java.awt.event.KeyEvent.VK_ENTER
import javax.swing.JLabel


internal class SpotifyWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val service = toolWindow.project.service<SpotifyService>()
    private val playlist = service.state

    private lateinit var nameLabel: Cell<JLabel>
    private lateinit var descLabel: Cell<JLabel>
    private lateinit var label: Cell<JLabel>
    private lateinit var bindText: Cell<JBTextField>
    private val url = service.authorizationCodeUri().toString()
//        "http://localhost:63342"

    val content = panel {
        panel {
            indent {
                row {
                    label("Authorization").apply { component.font = h1() }
                }
                row { cell(BrowserLink("Get code", url)) }
                row { label = label("") }
                row {
                    bindText = textField().apply {
                        component
                        component.document.addDocumentListener(PasteDocumentListener())
                        component.keyListener(
                            typed = { label.component.text += it.keyChar.toString() },
                            pressed = {},
                            released = {
                                val text = component.document.getText(0, component.document.length)
                                label.component.text = it.defaultActions(text)
                                when (it.keyChar) {
                                    VK_ENTER.toChar() -> service.setCode(label.component.text)
                                }
                            }
                        )
                        gap(RightGap.COLUMNS)
                    }
                    button("Authorize") {
                        service.authorizationCode()
                    }.apply {
                        component.foreground = JBColor.GREEN
                        component.font = h4()
                    }
                }
            }
        }
        panel {
            indent {
                row {
                    label("Playlist").apply {
                        component.font = h1()
                    }
                }
                row {
                    button(GET_PLAYLIST) {
                        service.loadState(playlist)
                        nameLabel.component.text = playlist.name
                        descLabel.component.text = playlist.description
                    }
                }
                row { nameLabel = label(playlist.name) }
                row { descLabel = label(playlist.description) }
            }
        }
        panel {
            indent {
                group("Tracks:") {
                    playlist.tracks.forEach { (name, href) ->
                        row {
                            button(name) {
                                service.addToQueue(href)
                            }.gap(RightGap.COLUMNS)
                        }
                    }
                }
            }
        }
    }

    override fun mayUseIndices(): Boolean = false


}
