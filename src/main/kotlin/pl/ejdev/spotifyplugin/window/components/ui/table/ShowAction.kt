package pl.ejdev.spotifyplugin.window.components.ui.table

import arrow.core.Either
import pl.ejdev.spotifyplugin.window.components.ui.dialog.dialog
import java.awt.Component
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JTable

internal class ShowAction(
    private val table: JTable,
    private val showAction: (name: String) -> Either<Component, Component>,
) : AbstractAction(Actions.SHOW.toString()) {
    override fun actionPerformed(event: ActionEvent) {
        val row = table.convertRowIndexToModel(table.editingRow)
        val value = table.model.getValueAt(row, 0) as String

        val component = showAction(value).getOrNull()!!
        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        dialog(screenSize.width / 2, screenSize.height / 2) {
            title = value
            showAction(value).getOrNull()?.let { add(it) }
        }
    }
}

