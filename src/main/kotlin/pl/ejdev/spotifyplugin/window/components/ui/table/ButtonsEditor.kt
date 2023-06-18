package pl.ejdev.spotifyplugin.window.components.ui.table

import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import java.awt.Component
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.AbstractCellEditor
import javax.swing.ButtonModel
import javax.swing.JButton
import javax.swing.JTable
import javax.swing.table.TableCellEditor

internal class ButtonsEditor(
    private val table: JTable,
    private val state: Array<SimplifiedPlaylistModel>,
) : AbstractCellEditor(), TableCellEditor {
    private val panel = ButtonsPanel()
    private var editorValue: Any? = null

    private inner class EditingStopHandler : MouseAdapter(), ActionListener {
        override fun mousePressed(event: MouseEvent) {
            when (event.source) {
                is TableCellEditor -> actionPerformed(null)

                is JButton -> {
                    if (
                        event.component.buttonModel.isPressed
                        && table.isRowSelected(table.editingRow)
                        && event.isControlDown
                    ) {
                        panel.background = table.background
                    }
                }
            }
        }

        override fun actionPerformed(event: ActionEvent?) = EventQueue.invokeLater { fireEditingStopped() }

        private val Component.buttonModel: ButtonModel
            get() = (this as JButton).model

    }

    init {
        panel.buttons[0].action = PlayAction(table)
        panel.buttons[1].action = ShowAction(table, state)
        val handler = EditingStopHandler()
        panel.buttons.forEach { button ->
            button.addMouseListener(handler)
            button.addActionListener(handler)
        }
        panel.addMouseListener(handler)
    }

    override fun getTableCellEditorComponent(
        table: JTable,
        value: Any,
        isSelected: Boolean,
        row: Int,
        column: Int
    ): Component {
        panel.background = table.selectionBackground
        panel.updateButtons(value)
        editorValue = value
        return panel
    }

    override fun getCellEditorValue(): Any = requireNotNull(editorValue)
}