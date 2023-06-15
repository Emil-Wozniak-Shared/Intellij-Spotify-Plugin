package pl.ejdev.spotifyplugin.window.components.ui

import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.util.NotNullFunction
import com.intellij.util.ui.UIUtil
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JList

fun <T> Row.jbList(content: List<T>, renderCell: NotNullFunction<T, JComponent>): Cell<JBList<T>> =
    JBList(content)
        .apply {
            cellRenderer = FunctionCellComponent(renderCell)
            actionMap.remove("copy")
        }
        .let { cell(it) }

class FunctionCellComponent<T>(
    private val renderer: NotNullFunction<T, JComponent>
) : DefaultListCellRenderer() {

    @Suppress("UNCHECKED_CAST")
    override fun getListCellRendererComponent(
        list: JList<*>,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component =
        renderer.`fun`(value as T).apply {
            isOpaque = true
            when {
                isSelected -> {
                    background = list.selectionBackground
                    foreground = list.selectionForeground
                }

                else -> {
                    background = list.background
                    foreground = list.foreground
                }
            }
            UIUtil.findComponentsOfType(this, JLabel::class.java).forEach { label ->
                label.foreground = UIUtil.getListForeground(isSelected, false)
            }
        }
}
