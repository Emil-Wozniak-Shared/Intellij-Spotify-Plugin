package pl.ejdev.spotifyplugin.window.components.ui.table

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JOptionPane
import javax.swing.JTable

internal class PlayAction(private val table: JTable) : AbstractAction(Actions.PLAY.toString()) {
    override fun actionPerformed(event: ActionEvent) {
        JOptionPane.showMessageDialog(table, "PLAYING")
    }
}