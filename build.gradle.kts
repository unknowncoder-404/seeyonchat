plugins {
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

repositories {
    mavenCentral()

    // Repositories Extension
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    // https://mvnrepository.com/artifact/com.vladsch.flexmark/flexmark
    implementation("com.vladsch.flexmark:flexmark:0.64.8")

    // https://mvnrepository.com/artifact/com.fifesoft/rsyntaxtextarea
    implementation("com.fifesoft:rsyntaxtextarea:3.4.0")

    // Dependencies Extension
    intellijPlatform {
        intellijIdeaCommunity("2022.3")

        javaCompiler()
    }
}

// IntelliJ Platform Extension
intellijPlatform {

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "223"
            untilBuild = provider { null }
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    buildSearchableOptions {
        enabled = false
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
