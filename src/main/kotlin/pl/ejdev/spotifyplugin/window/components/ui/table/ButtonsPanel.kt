package pl.ejdev.spotifyplugin.window.components.ui.table

import com.intellij.icons.AllIcons.Actions.Search
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import java.awt.FlowLayout
import java.util.*
import javax.swing.JButton
import javax.swing.JPanel

internal class ButtonsPanel : JPanel(FlowLayout(FlowLayout.LEFT)) {
    val buttons: MutableList<JButton> = mutableListOf()

    companion object {

        operator fun invoke(action: ButtonsPanel.() -> Unit): ButtonsPanel =
            ButtonsPanel().apply(action)
    }

    init {
        isOpaque = true
        Actions.values().forEach { action ->
            val icon = when (action) {
                Actions.PLAY -> Run
                Actions.SHOW -> Search
            }
            val button = JButton(icon)
                .apply {
                    isOpaque = false
                    isContentAreaFilled = false
                    isBorderPainted = false
                    isFocusable = false
                    isRolloverEnabled = false
                }
            add(button)
            buttons.add(button)
        }
    }

    fun updateButtons(value: Any) {
        if (value is EnumSet<*>) {
            removeAll()
            if (value.contains(Actions.PLAY)) {
                add(buttons[0])
            }
            if (value.contains(Actions.SHOW)) {
                add(buttons[1])
            }
        }
    }
}