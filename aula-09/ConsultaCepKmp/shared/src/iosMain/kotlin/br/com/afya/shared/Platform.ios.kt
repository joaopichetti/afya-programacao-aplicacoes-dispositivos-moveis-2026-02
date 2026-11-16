package br.com.afya.shared

import platform.UIKit.UIDevice

actual fun platform() = "${UIDevice.currentDevice.systemName()} " +
        UIDevice.currentDevice.systemVersion