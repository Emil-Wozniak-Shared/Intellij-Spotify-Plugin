package pl.ejdev.spotifyplugin.window.components.ui.table

import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import pl.ejdev.spotifyplugin.window.components.ui.dialog.dialog
import java.awt.Label
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JTable

internal class ShowAction(
    private val table: JTable,
    private val state: Array<SimplifiedPlaylistModel>,
) : AbstractAction(Actions.SHOW.toString()) {
    override fun actionPerformed(event: ActionEvent) {
        val row = table.convertRowIndexToModel(table.editingRow)
        val value = table.model.getValueAt(row, 0) as String

        val model = state.find { it.name == value }
        val href = model?.tracks?.href
        val total = model?.tracks?.total

        dialog {
            add(Label(value))
            add(Label("Total: $total"))
            add(Label("Href: $href"))
        }
    }
}

