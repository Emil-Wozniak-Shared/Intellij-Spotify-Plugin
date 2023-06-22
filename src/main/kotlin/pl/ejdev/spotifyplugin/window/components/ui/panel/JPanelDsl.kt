package pl.ejdev.spotifyplugin.window.components.ui.panel

import java.awt.Component
import java.awt.event.ActionListener
import javax.swing.GroupLayout
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPanel

class GroupLayoutDsl(
    private val panel: JPanel,
    private val autoCreateGaps: Boolean = true,
    private val autoCreateContainerGaps: Boolean = true
) {
    private val layout = GroupLayout(panel)
    private val rows = mutableListOf<Array<Component>>()

    init {
        panel.layout = layout
        layout.autoCreateGaps = autoCreateGaps
        layout.autoCreateContainerGaps = autoCreateContainerGaps
    }

    fun rows(builder: (MutableList<Array<Component>>) -> Unit) = apply {
        rows.apply(builder)
        layout.createParallelGroup(GroupLayout.Alignment.CENTER).also { horizontal ->
            horizontal.addGroup(
                layout.createSequentialGroup().apply {
                    rows.forEachIndexed { index, components ->
                        repeat((components.indices - 1).count()) {
                            addGroup(layout.createParallelGroup().apply {
                                components[index].let(::addComponent)
                            })
                        }
                    }
                }
            )
        }.let(layout::setHorizontalGroup)

        layout.createSequentialGroup().also { vertical ->
            vertical.addGroup(
                layout.createParallelGroup().apply {
                    rows.forEachIndexed { index, components ->
                        components[index].let(::addComponent)
                    }
                }
            )
        }.let(layout::setVerticalGroup)
    }
}

class JPanelDsl {
    val panel = JPanel()
    fun groupLayout(builder: GroupLayoutDsl.() -> Unit) = apply {
        GroupLayoutDsl(panel).apply(builder)
    }
}

fun jButton(text: String? = null, icon: Icon? = null, listener: ActionListener) =
    JButton(text, icon).apply {
        addActionListener(listener)
    }

fun jPanel(builder: JPanelDsl.() -> Unit) =
    JPanelDsl().also(builder).panel