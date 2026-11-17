package br.com.afya.shared

actual fun platform() = "${System.getProperty("os.name")} ${System.getProperty("os.version")} " +
        "(JVM ${System.getProperty("java.version")})"