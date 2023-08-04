/*
 *  Copyright 2023, TeamDev. All rights reserved.
 *
 *  Redistribution and use in source and/or binary forms, with or without
 *  modification, must retain the above copyright notice and the following
 *  disclaimer.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java

    // Provides convenience methods for adding JxBrowser dependencies into a project.
    id("com.teamdev.jxbrowser.gradle") version "0.0.3"

    // This plugin automatically resolves SWT dependencies.
    id("com.diffplug.gradle.eclipse.mavencentral") version "3.18.0"
}

val jxBrowserVersion by extra { "7.34" } // The version of JxBrowser used in the examples.
val guavaVersion by extra { "29.0-jre" } // Some of the examples use Guava.

allprojects {
    group = "com.teamdev.jxbrowser-examples"
    version = jxBrowserVersion
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.gradle.eclipse.mavencentral")
    apply(plugin = "com.teamdev.jxbrowser.gradle")

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenCentral()
    }

    jxbrowser {
        version = jxBrowserVersion
    }

    dependencies {
        // Cross-platform dependency
        implementation(jxbrowser.crossPlatform())

        /*
           For having only platform-dependent dependency:
           1. Comment out the cross-platform dependency above.
           2. Uncomment the dependency for your platform.
        */

        // Windows 32-bit
        // implementation(jxbrowser.win32())

        // Windows 64-bit
        // implementation(jxbrowser.win64())

        // macOS 64-bit
        // implementation(jxbrowser.mac())

        // macOS 64-bit ARM
        // implementation(jxbrowser.macArm())

        // Linux 64-bit
        // implementation(jxbrowser.linux64())

        // Linux 64-bit ARM
        // implementation(jxbrowser.linuxArm())

        // JavaFX dependency
        implementation(jxbrowser.javafx())

        // Swing dependency
        implementation(jxbrowser.swing())

        // SWT dependency
        implementation(jxbrowser.swt())

        // Depend on Guava for the Resources utility class used for loading resource files into strings.
        implementation("com.google.guava:guava:$guavaVersion")

        implementation(files("$rootDir/examples/src/main/resources/resource.jar"))
    }

    tasks.withType<JavaExec> {
        // Assign all Java system properties from
        // the command line to the JavaExec task.
        systemProperties(System.getProperties().mapKeys { it.key as String })
    }

    eclipseMavenCentral {
        // Plugin documentation claims that they support versions 3.5.0 through 4.12.0.
        // Nevertheless, all versions higher than 4.8.0 cannot be resolved.
        // Current version is bundled with the 3.107.0 version of the SWT.
        release("4.8.0") {
            implementation("org.eclipse.swt")
            useNativesForRunningPlatform()
        }
    }
}
