package pl.ejdev.spotifyplugin.window.components.section.authization

import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.util.ui.JBFont.h2
import mu.KotlinLogging
import org.cef.CefSettings
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefDisplayHandler
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.service.SpotifyAuthorizationService
import pl.ejdev.spotifyplugin.window.components.ui.h1label
import javax.swing.JLabel
import javax.swing.JPanel

private const val AUTHORIZATION = "Authorization"
private const val CODE_PATTERN = "?code="

lateinit var currentUserLabel: Cell<JLabel>

private val logger = KotlinLogging.logger { }

fun Panel.authorizationPanel(
    url: String,
    service: SpotifyAuthorizationService,
    userPlaylistSpotifyService: UserPlaylistSpotifyService,
): Panel = panel {
    indent {
        row { h1label(AUTHORIZATION) }
        row {
            currentUserLabel = label("").applyToComponent {
                isVisible = text != ""
                font = h2()
            }
        }
        row {
            cell(JPanel().apply browser@{
                add(JBCefBrowser(url).apply {
                    this.cefBrowser.client.addDisplayHandler(object : CefDisplayHandler {
                        override fun onAddressChange(browser: CefBrowser, frame: CefFrame, url: String) {
                            logger.warn { "Address: $url" }
                            if (url.contains(CODE_PATTERN)) {
                                service.setCode(url.split(CODE_PATTERN)[1])
                                service.loadState(service.state)
                                currentUserLabel.component.text = "You are logged in as: ${service.state.displayName}"
                                currentUserLabel.visible(service.state.displayName != "")
                                userPlaylistSpotifyService.loadState(userPlaylistSpotifyService.state)
                                this@browser.isVisible = false
                            }
                        }

                        override fun onTitleChange(browser: CefBrowser?, title: String?) {
                        }

                        override fun onTooltip(browser: CefBrowser?, text: String?): Boolean = true

                        override fun onStatusMessage(browser: CefBrowser?, value: String?) {
                        }

                        override fun onConsoleMessage(
                            browser: CefBrowser?,
                            level: CefSettings.LogSeverity?,
                            message: String?,
                            source: String?,
                            line: Int
                        ): Boolean = true

                        override fun onCursorChange(browser: CefBrowser?, cursorType: Int): Boolean = true
                    })
                }.component)
            })

        }
    }
}

