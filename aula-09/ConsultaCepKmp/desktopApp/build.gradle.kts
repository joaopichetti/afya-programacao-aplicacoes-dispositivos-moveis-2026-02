plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
}

dependencies {
    implementation(libs.kotlinx.coroutines.swing)
    implementation(compose.desktop.currentOs)
    implementation(project(":shared"))
}

compose.desktop {
    application {
        mainClass = "br.com.afya.consultacepkmp.desktop.MainKt"
    }
}
