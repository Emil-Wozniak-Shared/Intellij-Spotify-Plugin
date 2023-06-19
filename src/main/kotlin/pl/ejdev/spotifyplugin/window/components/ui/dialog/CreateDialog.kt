package pl.ejdev.spotifyplugin.window.components.ui.dialog

import com.intellij.ui.JBColor
import pl.ejdev.spotifyplugin.window.utils.centerOnScreen
import java.awt.Dialog
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JDialog

fun dialog(
    width: Int? = null,
    height: Int? = null,
    builder: Dialog.() -> Unit
) = JDialog().apply(builder).apply {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val componentWidth = (width ?: (screenSize.width / 2))
    val componentHeight = (height ?: (screenSize.height / 2))
    this.setSize(componentWidth, componentHeight)
    centerOnScreen(true, screenSize)
    isVisible = true
    background = JBColor.WHITE
}