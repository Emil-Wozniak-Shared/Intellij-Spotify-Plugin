package pl.ejdev.spotifyplugin.window.components.section.playlist

import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import pl.ejdev.spotifyplugin.model.PlaylistState
import pl.ejdev.spotifyplugin.service.SpotifyService
import pl.ejdev.spotifyplugin.window.components.ui.h1label
import pl.ejdev.spotifyplugin.window.configurations.GET_PLAYLIST
import javax.swing.JLabel

private lateinit var nameLabel: Cell<JLabel>
private lateinit var descLabel: Cell<JLabel>
fun Panel.playlistPanel(
    playlist: PlaylistState,
    service: SpotifyService
): Panel = panel {
    indent {
        row { h1label("Playlist") }
        row {
            button(GET_PLAYLIST) {
                service.loadState(playlist)
                nameLabel.component.text = playlist.name
                descLabel.component.text = playlist.description
            }
        }
        row { nameLabel = label(playlist.name) }
        row { descLabel = label(playlist.description) }
    }
}