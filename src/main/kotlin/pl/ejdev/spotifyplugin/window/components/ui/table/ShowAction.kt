package pl.ejdev.spotifyplugin.window.components.ui.table

import arrow.core.Either
import pl.ejdev.spotifyplugin.window.components.ui.dialog.dialog
import java.awt.Component
import java.awt.Label
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

        dialog {
            add(Label(value))
            showAction(value)
                .onRight(::add)
                .onLeft(::add)
        }
    }
}

