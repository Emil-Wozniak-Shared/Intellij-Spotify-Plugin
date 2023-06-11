package pl.ejdev.spotifyplugin.window.components.section.authization

import com.intellij.ui.JBColor
import com.intellij.ui.components.BrowserLink
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.Row
import com.intellij.util.ui.JBFont
import pl.ejdev.spotifyplugin.extensions.listeners.defaultActions
import pl.ejdev.spotifyplugin.extensions.listeners.keyListener
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.h1label
import pl.ejdev.spotifyplugin.window.listeners.PasteDocumentListener
import java.awt.event.KeyEvent
import javax.swing.JLabel

private const val AUTHORIZE = "Authorize"
private const val AUTHORIZATION = "Authorization"
private const val GET_CODE = "Get code"

private lateinit var bindText: Cell<JBTextField>
private lateinit var label: Cell<JLabel>

fun Panel.authorizationPanel(
    url: String,
    service: SpotifyService,
): Panel = panel {
    indent {
        row { h1label(AUTHORIZATION) }
        row { cell(BrowserLink(GET_CODE, url)) }
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
                            KeyEvent.VK_ENTER.toChar() -> {
                                service.setCode(label.component.text)
                                service.authorizationCode()
                            }
                        }
                    }
                )
                gap(RightGap.COLUMNS)
            }
            button(AUTHORIZE) {
                service.setCode(label.component.text)
                service.authorizationCode()
            }.apply {
                component.foreground = JBColor.GREEN
                component.font = JBFont.h4()
            }
        }
    }
}