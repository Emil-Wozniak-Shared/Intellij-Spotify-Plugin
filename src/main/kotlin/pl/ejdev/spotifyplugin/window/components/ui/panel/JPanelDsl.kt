package pl.ejdev.spotifyplugin.window.components.ui.panel

import java.awt.Adjustable.HORIZONTAL
import java.awt.Component
import java.awt.event.ActionListener
import javax.swing.GroupLayout
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPanel

/**
 * ```kotlin
 * jPanel {
 *    groupLayout {
 *        rows { rows ->
 *            playlistSpotifyService.getPlaylist(requireNotNull(id)).onRight { state ->
 *                state.tracks.map { (name, href) ->
 *                    rows.add(
 *                        arrayOf(
 *                            jButton(icon = Run) {
 *                                playlistSpotifyService.addToQueue(href)
 *                            }.apply {
 *                                preferredSize = Dimension(50, 30)
 *                            },
 *                            JLabel(name).apply {
 *                                preferredSize = Dimension(100, 30)
 *                            }
 *                        )
 *                    )
 *                }
 *            }
 *        }
 *    }
 * }
 * ```
 */
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
        layout.linkSize(HORIZONTAL, *rows.map(Array<Component>::first).toTypedArray())
        layout.createParallelGroup(GroupLayout.Alignment.CENTER).also { horizontal ->
            horizontal.addGroup(
                layout.createSequentialGroup().also { sequentialGroup ->
                    sequentialGroup.addGroup(layout.createParallelGroup().apply {
                        rows.forEach { components ->
                            components.forEach(::addComponent)
                        }
                    })
                }
            )
        }.let(layout::setHorizontalGroup)

        layout.createSequentialGroup().also { vertical ->
            rows.forEach { components ->
                vertical.addGroup(
                    layout.createParallelGroup().apply {
                        components.forEach(::addComponent)
                    }
                )
            }
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

fun jPanel(builder: JPanelDsl.() -> Unit): JPanel {
    return JPanelDsl().also(builder).panel
}