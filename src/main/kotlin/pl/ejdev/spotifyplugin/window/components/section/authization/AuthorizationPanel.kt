package pl.ejdev.spotifyplugin.window.components.section.authization

import com.intellij.ide.BrowserUtil
import com.intellij.ui.JBColor
import com.intellij.ui.components.ActionLink
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.util.ui.JBFont
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.extensions.listeners.defaultActions
import pl.ejdev.spotifyplugin.extensions.listeners.keyListener
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.h1label
import pl.ejdev.spotifyplugin.window.listeners.PasteDocumentListener
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
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
        row {
            cell(ActionLink().apply { this.text = GET_CODE }).applyToComponent {
                val listener = CallbackListener(url, service)
                this.addActionListener(listener)

            }
        }
        row { label = label("") }
        row {
            bindText = textField().applyToComponent {
                document.addDocumentListener(PasteDocumentListener())
                keyListener(
                    typed = { label.component.text += it.keyChar.toString() },
                    pressed = {},
                    released = {
                        val text = document.getText(0, document.length)
                        label.component.text = it.defaultActions(text)
                        when (it.keyChar) {
                            KeyEvent.VK_ENTER.toChar() -> {
                                service.setCode(label.component.text)
                                service.authorizationCode()
                            }
                        }
                    }
                )
            }.gap(RightGap.COLUMNS)
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

private const val CALLBACK_CODE = "callback?code="

class CallbackListener(val url: String, val service: SpotifyService) : ActionListener {
    var code = ""
    private val logger = KotlinLogging.logger { }
    override fun actionPerformed(event: ActionEvent) {
        BrowserUtil.browse(url)
        val serverSocket = ServerSocket(8888)
        val clientSocket = serverSocket.accept()
        val out = PrintWriter(clientSocket.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val output = input.readLine()
        if (output.contains(CALLBACK_CODE)) {
            code = output.split(CALLBACK_CODE)[1]
            service.setCode(code)
            service.authorizationCode()
            logger.warn { "code=$code" }
        }
    }
}

