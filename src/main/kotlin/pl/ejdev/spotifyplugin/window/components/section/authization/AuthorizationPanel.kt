package pl.ejdev.spotifyplugin.window.components.section.authization

import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.jcef.JBCefBrowser
import mu.KotlinLogging
import org.cef.CefSettings
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefDisplayHandler
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.h1label
import javax.swing.JPanel

private const val AUTHORIZATION = "Authorization"
private const val CODE_PATTERN = "?code="

private val logger = KotlinLogging.logger { }

fun Panel.authorizationPanel(url: String, service: SpotifyService): Panel = panel {
    indent {
        row { h1label(AUTHORIZATION) }
        row {
            cell(JPanel().apply browser@{
                add(JBCefBrowser(url).apply {
                    this.cefBrowser.client.addDisplayHandler(object : CefDisplayHandler {
                        override fun onAddressChange(browser: CefBrowser, frame: CefFrame, url: String) {
                            logger.warn { "Address: $url" }
                            if (url.contains(CODE_PATTERN)) {
                                service.setCode(url.split(CODE_PATTERN)[1])
                                service.authorizationCode()
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

