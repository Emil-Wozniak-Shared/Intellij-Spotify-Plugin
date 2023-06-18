package pl.ejdev.spotifyplugin.window.components.ui.table

import java.awt.Component
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

internal class ButtonsRenderer : TableCellRenderer {
    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component = ButtonsPanel {
        background =
            if (isSelected) table.selectionBackground
            else table.background
        updateButtons(value)
    }
}