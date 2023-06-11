package pl.ejdev.spotifyplugin.window.components.section.track

import com.intellij.icons.AllIcons.Actions.Play_forward
import com.intellij.ui.JBColor
import com.intellij.ui.JBColor.GREEN
import com.intellij.ui.RoundedLineBorder
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RightGap
import pl.ejdev.spotifyplugin.model.PlaylistState
import pl.ejdev.spotifyplugin.service.SpotifyService
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JLabel
import javax.swing.border.Border

fun Panel.tracksPanel(
    playlist: PlaylistState,
    service: SpotifyService
): Panel = panel {
    indent {
        group("Tracks:") {
            playlist.tracks.forEach { (name, href) ->
                row {
                    icon(Play_forward)
                        .applyToComponent {
                            border = RoundedLineBorder(GREEN, 25, 1)
                            onClickListener(service, href, border as RoundedLineBorder)
                        }
                        .gap(RightGap.SMALL)
                    label(name)
                }
            }
        }
    }
}

private fun JLabel.onClickListener(service: SpotifyService, href: String, border: RoundedLineBorder) {
    this.addMouseListener(object : MouseListener {
        override fun mouseClicked(e: MouseEvent) {
        }

        override fun mousePressed(e: MouseEvent) {
            service.addToQueue(href)
                .onLeft { border.setColor(JBColor.RED) }
        }

        override fun mouseReleased(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseExited(e: MouseEvent) {}
    })
}