package pl.ejdev.spotifyplugin.window.components.ui.button

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Icon
import javax.swing.JButton

fun iconButton(
    icon: Icon, actionListener: (event: ActionEvent) -> Unit = {
        ActionListener { }
    }
): JButton =
    JButton(icon)
        .apply {
            isOpaque = false
            isContentAreaFilled = false
            isBorderPainted = false
            isFocusable = false
            isRolloverEnabled = false
            addActionListener(ActionListener(actionListener))
        }
