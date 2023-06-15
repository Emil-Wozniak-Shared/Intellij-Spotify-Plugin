package pl.ejdev.spotifyplugin.window.components.section.playlist

import com.intellij.icons.AllIcons.Actions.Play_back
import com.intellij.icons.AllIcons.Actions.Play_forward
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBList
import com.intellij.ui.components.Label
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.util.ui.JBFont.h2
import mu.KotlinLogging
import pl.ejdev.spotifyplugin.model.SimplifiedPlaylistModel
import pl.ejdev.spotifyplugin.service.UserPlaylistSpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.jbList
import javax.swing.Icon
import javax.swing.JButton

private lateinit var playlistComponent: Cell<JBList<String>>
private lateinit var playIcon: Icon

private val logger = KotlinLogging.logger {  }

fun Panel.playlistPanel(userPlaylistSpotifyService: UserPlaylistSpotifyService): Panel =
    panel {
        indent {
            row {
                button("User Playlists") {
                    userPlaylistSpotifyService.loadState(userPlaylistSpotifyService.state)
                    playlistComponent.component.model = JBList.createDefaultListModel(
                        userPlaylistSpotifyService.state.map(SimplifiedPlaylistModel::name)
                    )
                }
            }
            row {
                playlistComponent = jbList(userPlaylistSpotifyService.state.mapNotNull(SimplifiedPlaylistModel::name)) {
                    JBBox.createHorizontalBox().apply {
                        playIcon = Play_forward.apply { font = h2() }
                        setComponentZOrder(JButton(playIcon).apply {
                            this.pressedIcon = Play_back
                            this.addActionListener {
                                logger.warn {  "lorem ipsum" }
                            }
                        }, 0)
                        setComponentZOrder(Label(it), 1)
                    }
                }
            }
        }
    }

