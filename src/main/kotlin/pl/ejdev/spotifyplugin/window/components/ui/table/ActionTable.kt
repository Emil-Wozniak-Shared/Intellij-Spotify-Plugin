package pl.ejdev.spotifyplugin.window.components.ui.table

import arrow.core.Either
import com.intellij.ui.table.JBTable
import java.awt.Component
import javax.swing.table.DefaultTableModel

fun actionTableModel(
    data: Array<Array<Any>>,
    column1: String,
    column2: String,
): DefaultTableModel = object : DefaultTableModel(data, arrayOf(column1, column2)) {
    override fun getColumnClass(column: Int): Class<*> = getValueAt(0, column).javaClass
}

private val defaultColumnNames = arrayOf("", "")
private val defaultTableData = arrayOf<Array<Any>>()

fun actionTable(): JBTable =
    JBTable(object : DefaultTableModel(defaultTableData, defaultColumnNames) {
        override fun getColumnClass(column: Int): Class<*> = getValueAt(0, column).javaClass
    })

fun JBTable.actionTableColumnRenderer(showAction: (name: String) -> Either<Component, Component>) = also { table ->
    rowHeight = 36
    visibleRowCount = model.columnCount
    columnModel.getColumn(1).run {
        cellRenderer = ButtonsRenderer()
        cellEditor = ButtonsEditor(table, showAction)
        width = 300
    }
}
