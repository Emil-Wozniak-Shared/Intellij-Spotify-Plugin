package pl.ejdev.spotifyplugin.window.components.ui.image

import java.awt.EventQueue
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JLabel

fun jImage(url: URL): JLabel {
    val label = JLabel()
    EventQueue.invokeLater {
        try {
            val image: BufferedImage = ImageIO.read(url)
            label.setBounds(image.raster.minX, image.raster.minY, image.width / 2, image.height / 2)
            val scaleImage: Image =
                ImageIcon(image).image.getScaledInstance(image.width / 4, image.height / 4, Image.SCALE_DEFAULT)
            label.icon = ImageIcon(scaleImage)
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }
    return label
}