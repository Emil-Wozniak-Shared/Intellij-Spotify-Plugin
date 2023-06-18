package pl.ejdev.spotifyplugin.window.components.section.playlist

import arrow.core.raise.either
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.table.JBTable
import net.miginfocom.swing.MigLayout
import pl.ejdev.spotifyplugin.service.PlaylistSpotifyService
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.button.iconButton
import pl.ejdev.spotifyplugin.window.components.ui.table.Actions
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTable
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableColumnRenderer
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableModel
import java.awt.Label
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel
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
                            playlistState.find { it.name == name }
                                ?.let { playlist ->
                                    val panel = AwtPanel()
                                    playlist.id
                                        ?.let(playlistSpotifyService::fetchPlaylist)
                                        ?.onRight { playlistState ->
                                            panel.add(Label(playlistState.name))
                                            playlistState.tracks.forEach { (name, href) ->
                                                panel.add(AwtPanel().apply {
                                                    add(iconButton(Run) {
                                                        playlistSpotifyService.addToQueue(href)
                                                    })
                                                    add(JLabel(name))
                                                })
                                            }
                                        }
                                    panel
                                }
                                ?: raise(AwtPanel())
                        }
                    }

                }
            }
        }
        row {
            table = actionTable().actionTableColumnRenderer { either { AwtPanel() } }
            cell(
                JPanel(MigLayout(LAYOUT_CONSTRAINS, COLUMNS_CONSTRAINS))
                    .apply { add(JBScrollPane(table)) }
            )
        }
    }
