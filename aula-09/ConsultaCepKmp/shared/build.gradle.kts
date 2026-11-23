plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.android.lint)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.ksp)
  alias(libs.plugins.androidx.room)
}

kotlin {

  // Necessário para o "expect object AppDatabaseConstructor" exigido pelo Room
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }

  // Target declarations - add or remove as needed below. These define
  // which platforms this KMP module supports.
  // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
  android {
    namespace = "br.com.afya.shared"
    compileSdk {
      version = release(37)
    }
    minSdk = 26

    withHostTestBuilder {
    }

    withDeviceTestBuilder {
      sourceSetTreeName = "test"
    }.configure {
      instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  }

  // For iOS targets, this is also where you should
  // configure native binary output. For more information, see:
  // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

  // A step-by-step guide on how to include this library in an XCode
  // project can be found here:
  // https://developer.android.com/kotlin/multiplatform/migrate
  val xcfName = "sharedKit"

  iosArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }

  iosSimulatorArm64 {
    binaries.framework {
      baseName = xcfName
    }
  }
  // Target JVM consumido pela aplicação Desktop (:desktopApp)
  jvm()

  // Source set declarations.
  // Declaring a target automatically creates a source set with the same name. By default, the
  // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
  // common to share sources between related targets.
  // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlin.stdlib)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.logging)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.compose.multiplatform.runtime)
        implementation(libs.compose.multiplatform.foundation)
        implementation(libs.compose.multiplatform.ui)
        implementation(libs.compose.multiplatform.ui.tooling.preview)
        implementation(libs.compose.multiplatform.material3)
        implementation(libs.compose.multiplatform.material.icons.core)
        implementation(libs.androidx.lifecycle.viewmodel.compose.multiplatform)
        implementation(libs.navigation.compose.multiplatform)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.sqlite.bundled)
        implementation(libs.kotlinx.datetime)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotlin.test)
      }
    }

    androidMain {
      dependencies {
        implementation(libs.ktor.client.okhttp)
      }
    }

    getByName("androidDeviceTest") {
      dependencies {
        implementation(libs.androidx.core)
        implementation(libs.androidx.junit)
        implementation(libs.androidx.runner)
      }
    }

    iosMain {
      dependencies {
        implementation(libs.ktor.client.darwin)
      }
    }

    jvmMain {
      dependencies {
        implementation(libs.ktor.client.okhttp)
      }
    }
  }
}

// Diretório onde o Room exporta o schema do banco (útil para versionar e criar migrations)
room {
  schemaDirectory("$projectDir/schemas")
}

dependencies {
  // Permite renderizar os @Preview do commonMain no Android Studio
  "androidRuntimeClasspath"(libs.compose.multiplatform.ui.tooling)
  // Processador de anotações do Room (KSP), executado para cada target da aplicação
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspJvm", libs.androidx.room.compiler)
  add("kspIosArm64", libs.androidx.room.compiler)
  add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}