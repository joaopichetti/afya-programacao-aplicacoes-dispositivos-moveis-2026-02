package br.com.afya.consultacepkmp.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.afya.shared.platform
import br.com.afya.shared.ui.App

fun main() {
    println("Hello from shared module: ${platform()}")

    // Integra o nome do app ao macOS
    System.setProperty("apple.awt.application.name", "Consulta de CEP")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Consulta de CEP",
            state = rememberWindowState(width = 480.dp, height = 720.dp)
        ) {
            App()
        }
    }
}