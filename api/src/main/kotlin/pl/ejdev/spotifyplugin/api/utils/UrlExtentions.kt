package pl.ejdev.spotifyplugin.api.utils

import java.net.URL

@Suppress("UNCHECKED_CAST")
fun <T> URL.connectAs(actions: T.() -> Unit): T = (this.openConnection() as T).apply(actions)