package pl.ejdev.spotifyplugin.listeners

import pl.ejdev.spotifyplugin.extensions.removeLast
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

inline fun <reified T: Component> T.keyListener(
    crossinline typed: (event: KeyEvent) -> Unit = {},
    crossinline pressed: (event: KeyEvent) -> Unit = {},
    crossinline released: (event: KeyEvent) -> Unit = {},
) {
    this.addKeyListener(
        object : KeyListener {
            override fun keyTyped(event: KeyEvent) = typed(event)
            override fun keyPressed(event: KeyEvent) = pressed(event)
            override fun keyReleased(event: KeyEvent) = released(event)
        }
    )
}

fun KeyEvent.defaultActions(text: String): String = when (keyChar) {
    KeyEvent.VK_BACK_SPACE.toChar() -> text.removeLast()
    else -> text
}