package pl.ejdev.spotifyplugin.window.listeners

import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

internal class PasteDocumentListener : DocumentListener {
    override fun changedUpdate(event: DocumentEvent) {}
    override fun insertUpdate(event: DocumentEvent) {
        try {
            val text: String = event.document.getText(0, event.document.length)
        } catch (exception: BadLocationException) {
            exception.printStackTrace()
            return
        }
    }

    override fun removeUpdate(event: DocumentEvent) {}
}