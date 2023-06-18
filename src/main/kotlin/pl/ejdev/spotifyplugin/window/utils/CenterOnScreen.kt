package pl.ejdev.spotifyplugin.window.utils

import java.awt.Component
import java.awt.Dimension
import java.awt.Toolkit

fun Component.centerOnScreen(absolute: Boolean, screenSize: Dimension) {
    val width: Int = this.width
    val height: Int = this.height
    var x = screenSize.width / 2 - width / 2
    var y = screenSize.height / 2 - height / 2
    if (!absolute) {
        x /= 2
        y /= 2
    }
    this.setLocation(x, y)
}