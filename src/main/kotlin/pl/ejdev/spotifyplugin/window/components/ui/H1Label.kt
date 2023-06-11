package pl.ejdev.spotifyplugin.window.components.ui

import com.intellij.ui.dsl.builder.Row
import com.intellij.util.ui.JBFont

fun Row.h1label(text: String) = this.label(text).apply { component.font = JBFont.h1() }