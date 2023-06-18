package pl.ejdev.spotifyplugin.window.components.section.playlist

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.table.JBTable
import net.miginfocom.swing.MigLayout
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.table.Actions
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTable
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableColumnRenderer
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableModel
import java.util.*
import javax.swing.JPanel

private const val GET_PLAYLISTS_BUTTON_NAME = "Get Playlists"
private const val COLUMN_1_NAME = "Playlists"
private const val COLUMN_2_NAME = "Actions"

private const val LAYOUT_CONSTRAINS = "wrap 2"
private const val COLUMNS_CONSTRAINS = "[600:pref, fill, grow][20%]"

private lateinit var table: JBTable

fun Panel.playlistPanel(userPlaylistSpotifyService: UserPlaylistSpotifyService): Panel =
    panel {
        indent {
            row {
                button(GET_PLAYLISTS_BUTTON_NAME) {
                    userPlaylistSpotifyService.loadState(userPlaylistSpotifyService.state)
                    val model = actionTableModel(
                        data = userPlaylistSpotifyService.state
                            .filter { it.name != null }
                            .map { arrayOf(it.name, EnumSet.allOf(Actions::class.java)) }
                            .filterIsInstance<Array<Any>>()
                            .toTypedArray(),
                        column1 = COLUMN_1_NAME,
                        column2 = COLUMN_2_NAME
                    )
                    table.model = model
                    table.actionTableColumnRenderer( userPlaylistSpotifyService.state)
                }
            }
        }
        row {
            table = actionTable().actionTableColumnRenderer(userPlaylistSpotifyService.state)
            cell(
                JPanel(MigLayout(LAYOUT_CONSTRAINS, COLUMNS_CONSTRAINS))
                    .apply { add(JBScrollPane(table)) }
            )
        }
    }
