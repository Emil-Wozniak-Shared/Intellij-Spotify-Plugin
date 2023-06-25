package pl.ejdev.spotifyplugin.window.components.section.playlist

import arrow.core.raise.either
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.table.JBTable
import pl.ejdev.spotifyplugin.api.service.player.PlayAction
import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import pl.ejdev.spotifyplugin.service.PlayerSpotifyService
import pl.ejdev.spotifyplugin.service.PlaylistSpotifyService
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.image.jImage
import pl.ejdev.spotifyplugin.window.components.ui.panel.jButton
import pl.ejdev.spotifyplugin.window.components.ui.panel.jPanel
import pl.ejdev.spotifyplugin.window.components.ui.table.Actions
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTable
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableColumnRenderer
import pl.ejdev.spotifyplugin.window.components.ui.table.actionTableModel
import pl.ejdev.spotifyplugin.window.utils.screenSize
import java.awt.Adjustable.HORIZONTAL
import java.awt.Dimension
import java.net.URL
import java.util.*
import javax.swing.BoxLayout
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.CENTER
import javax.swing.JLabel
import javax.swing.JPanel

private const val GET_PLAYLISTS_BUTTON_NAME = "Get Playlists"
private const val COLUMN_1_NAME = "Playlists"
private const val COLUMN_2_NAME = "Actions"

private val PLAYLIST_TABLE_PREFERRED_SIZE = Dimension(Int.MAX_VALUE, screenSize.height / 2)

private lateinit var table: JBTable

fun Panel.playlistPanel(
    userPlaylistSpotifyService: UserPlaylistSpotifyService,
    playlistSpotifyService: PlaylistSpotifyService,
    playerSpotifyService: PlayerSpotifyService
): Panel =
    panel {
        indent {
            row {
                button(GET_PLAYLISTS_BUTTON_NAME) {
                    userPlaylistSpotifyService.loadState(userPlaylistSpotifyService.state)
                    val playlistState = userPlaylistSpotifyService.state
                    val model = actionTableModel(
                        data = playlistState
                            .mapNotNull(SimplifiedPlaylistModel::name)
                            .map { arrayOf(it, EnumSet.allOf(Actions::class.java)) }
                            .filterIsInstance<Array<Any>>()
                            .toTypedArray(),
                        names = arrayOf(COLUMN_1_NAME, COLUMN_2_NAME)
                    )
                    table.model = model
                    table.actionTableColumnRenderer { name ->
                        either {
                            playlistState.find { it.name == name }
                                ?.run { tracksPanel(playlistSpotifyService, playerSpotifyService) }
                                ?: raise(jPanel { })
                        }
                    }
                }
            }
        }
        row {
            table = actionTable().actionTableColumnRenderer { either { jPanel { } } }
            cell(
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
                    preferredSize = PLAYLIST_TABLE_PREFERRED_SIZE
                    add(JBScrollPane(table).apply {
                        preferredSize = PLAYLIST_TABLE_PREFERRED_SIZE
                    })
                }
            )
        }
    }

private fun SimplifiedPlaylistModel.tracksPanel(
    playlistSpotifyService: PlaylistSpotifyService,
    playerSpotifyService: PlayerSpotifyService,
) = jPanel {
    val image = trackImage()
    val layout = GroupLayout(this.panel)
    this.panel.layout = layout
    layout.autoCreateGaps = true
    layout.autoCreateContainerGaps = true
    playlistSpotifyService.getPlaylist(requireNotNull(id)).onRight { state ->
        val rows = state.tracks.map { track ->
            arrayOf(
                jButton(icon = Run) {
                    playlistSpotifyService.addToQueue(track.href)
//                    playerSpotifyService.performPlaylistAction(AddItemToQueue(track.href))
                    playerSpotifyService.performAction(PlayAction)
                },
                JLabel(track.name)
            )
        }
        layout.linkSize(HORIZONTAL, *rows.map { (button, _) -> button }.toTypedArray())
        layout.createParallelGroup(CENTER).also { horizontal ->
            horizontal.addComponent(image)
            horizontal.addGroup(
                layout.createSequentialGroup().also { group ->
                    group.addGroup(layout.createParallelGroup().apply {
                        rows.forEach { (button, _) -> addComponent(button) }
                    })
                    group.addGroup(layout.createParallelGroup().apply {
                        rows.forEach { (_, label) -> addComponent(label) }
                    })
                }
            ).let(layout::setHorizontalGroup)
        }
        layout.createSequentialGroup().also { vertical ->
            vertical.addComponent(image)
            rows.forEach { (button, label) ->
                vertical.addGroup(
                    layout.createParallelGroup()
                        .addComponent(button)
                        .addComponent(label)
                )
            }
        }.let(layout::setVerticalGroup)
    }
}.let(::JBScrollPane)

private fun SimplifiedPlaylistModel.trackImage() = (images.firstOrNull()
    ?.url
    ?.let(::URL)
    ?.let(::jImage)
    ?: JLabel())
