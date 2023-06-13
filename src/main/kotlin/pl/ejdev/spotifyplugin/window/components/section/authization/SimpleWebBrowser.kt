package pl.ejdev.spotifyplugin.window.components.section.authization

import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers.ofString
import javax.swing.*
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener


/**
 * Defines a simple web browser that can load web pages from
 * URLs specified by the user in a "Location" box.  Almost all
 * the functionality is provided automatically by the JEditorPane
 * class.  The program loads web pages synchronously and can hang
 * indefinitely while trying to load a page.  See
 * SimpleWebBrowserWithThread for an asynchronous version.
 * This class can be run as a standalone application and has
 * a nested class that can be run as an applet.  The applet
 * version can probably only read pages from the server from which
 * it was loaded.
 */
class SimpleWebBrowser(var url: String) : JComponent() {
    /**
     * The pane in which documents are displayed.
     */
    private val editPane: JEditorPane

    /**
     * An input box where the user enters the URL of a document
     * to be loaded into the edit pane.  A valid URL string has
     * to contain the substring "://".  If the string in the box
     * does not contain this substring, then "http://" is
     * prepended to the string.
     */
    private val locationInput: JTextField

    /**
     * Defines a listener that responds when the user clicks on
     * a link in the document.
     */
    private inner class LinkListener : HyperlinkListener {
        override fun hyperlinkUpdate(event: HyperlinkEvent) {
            if (event.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                loadURL(event.url.readText())
            }
        }
    }

    /**
     * Defines a listener that loads a new page when the user
     * clicks the "Go" button or presses return in the location
     * input box.
     */
    private inner class GoListener : ActionListener {
        override fun actionPerformed(evt: ActionEvent) {
            try {
                var location = locationInput.text.trim { it <= ' ' }
                if (location.isEmpty()) throw Exception()
                if (!location.contains("://")) location = "http://$location"
                url = location
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(
                    this@SimpleWebBrowser,
                    "The Location input box does not\ncontain a legal URL."
                )
                return
            }
            loadURL(url)
            locationInput.selectAll()
            locationInput.requestFocus()
        }
    }

    /**
     * Construct a panel that contains a JEditorPane in a JScrollPane,
     * with a tool bar that has a Location input box and a Go button.
     */
    init {
        val goListener: ActionListener = GoListener()
        background = JBColor.BLACK
        layout = BorderLayout(1, 1)
        border = BorderFactory.createLineBorder(JBColor.BLACK, 1)
        editPane = JEditorPane().apply {
            isEditable = false
            addHyperlinkListener(LinkListener())
            contentType = "text/plain"
            text = """
                lorem ipsum
            """.trimIndent()
        }
        locationInput = JTextField(url, 40).apply {
            addActionListener(goListener)
        }
        add(JScrollPane(editPane), BorderLayout.CENTER)
        val toolbar = JToolBar().apply {
            isFloatable = false
            add(locationInput)
            addSeparator(Dimension(5, 0))
            add(JButton(" Go ").apply { addActionListener(goListener) })
        }
        add(toolbar, BorderLayout.NORTH)
    }

    /**
     * Loads the document at the specified URL into the edit pane.
     */
    private fun loadURL(url: String) {
        try {
            val body = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
                .send(
                    HttpRequest.newBuilder().GET().uri(URI(url)).build(),
                    ofString()
                )
                .body()
            editPane.page = URL(url)
            editPane.contentType = "text/html"
            editPane.text = body
        } catch (e: Exception) {
            editPane.contentType = "text/plain"
            editPane.text = """
                Sorry, the requested document was not found
                or cannot be displayed.
                
                Error:$e
                """.trimIndent()
        }
    }


    fun run(url: String) {
        val window = JFrame("SimpleWebBrowser")
        val content = SimpleWebBrowser(url)
        window.contentPane = content
        window.setSize(600, 500)
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        window.setLocation(
            (screenSize.width - window.width) / 2,
            (screenSize.height - window.height) / 2
        )
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        window.isVisible = true
    }

}