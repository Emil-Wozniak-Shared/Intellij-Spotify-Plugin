package pl.ejdev.spotifyplugin.window.components.section.playlist

import arrow.core.raise.either
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.table.JBTable
import net.miginfocom.swing.MigLayout
import pl.ejdev.spotifyplugin.service.PlaylistSpotifyService
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.panel.jButton
import pl.ejdev.spotifyplugin.window.components.ui.panel.jPanel
import pl.ejdev.spotifyplugin.window.components.ui.table.Actions
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTable
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableColumnRenderer
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableModel
import java.util.*
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.CENTER
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants.HORIZONTAL
import java.awt.Panel as AwtPanel


private const val GET_PLAYLISTS_BUTTON_NAME = "Get Playlists"
private const val COLUMN_1_NAME = "Playlists"
private const val COLUMN_2_NAME = "Actions"

private const val LAYOUT_CONSTRAINS = "wrap 2"
private const val COLUMNS_CONSTRAINS = "[600:pref, fill, grow][20%]"

private lateinit var table: JBTable

fun Panel.playlistPanel(
    userPlaylistSpotifyService: UserPlaylistSpotifyService,
    playlistSpotifyService: PlaylistSpotifyService
): Panel =
    panel {
        indent {
            row {
                button(GET_PLAYLISTS_BUTTON_NAME) {
                    userPlaylistSpotifyService.loadState(userPlaylistSpotifyService.state)
                    val playlistState = userPlaylistSpotifyService.state
                    val model = actionTableModel(
                        data = playlistState
                            .filter { it.name != null }
                            .map { arrayOf(it.name, EnumSet.allOf(Actions::class.java)) }
                            .filterIsInstance<Array<Any>>()
                            .toTypedArray(),
                        column1 = COLUMN_1_NAME,
                        column2 = COLUMN_2_NAME
                    )
                    table.model = model
                    table.actionTableColumnRenderer { name ->
                        either {
                            playlistState.find { it.name == name }?.run {
                                jPanel {
                                    groupLayout {
                                        rows { rows ->
                                            playlistSpotifyService.getPlaylist(requireNotNull(id)).onRight { state ->
                                                state.tracks.map { (name, href) ->
                                                    rows.add(
                                                        arrayOf(
                                                            jButton(icon = Run) {
                                                                playlistSpotifyService.addToQueue(href)
                                                            },
                                                            JLabel(name)
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                JPanel().apply {
                                    val layout = GroupLayout(this)
                                    this.layout = layout
                                    layout.autoCreateGaps = true
                                    layout.autoCreateContainerGaps = true
                                    playlistSpotifyService.getPlaylist(requireNotNull(id)).onRight { state ->
                                        val rows = state.tracks.map { (name, href) ->
                                            JButton(Run).apply {
                                                addActionListener { playlistSpotifyService.addToQueue(href) }
                                            } to JLabel(name)
                                        }
                                        layout.linkSize(
                                            HORIZONTAL,
                                            *rows.map { (button, _) -> button }.toTypedArray()
                                        )
                                        layout.createParallelGroup(CENTER).also { horizontal ->
                                            horizontal.addGroup(
                                                layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup().apply {
                                                        rows.forEach { (button, _) -> addComponent(button) }
                                                    })
                                                    .addGroup(layout.createParallelGroup().apply {
                                                        rows.forEach { (_, label) -> addComponent(label) }
                                                    })
                                            ).let(layout::setHorizontalGroup)
                                        }
                                        layout.createSequentialGroup().also { vertical ->
                                            rows.forEach { (button, label) ->
                                                vertical.addGroup(
                                                    layout.createParallelGroup()
                                                        .addComponent(button)
                                                        .addComponent(label)
                                                )
                                            }
                                        }.let(layout::setVerticalGroup)
                                    }
                                }
                            } ?: raise(AwtPanel())
                        }
                    }
                }
            }
        }
        row {
            table = actionTable().actionTableColumnRenderer { either { AwtPanel() } }
            cell(JPanel(MigLayout(LAYOUT_CONSTRAINS, COLUMNS_CONSTRAINS)).apply { add(JBScrollPane(table)) })
        }
    }
