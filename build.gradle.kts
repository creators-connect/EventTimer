import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    application
    id("io.freefair.lombok") version "8.4"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

group = "at.quer3000"
version = "1.0"
val apiVersion = "1.20.4"  // Changed to full version number

application {
    mainClass.set("at.querlenker.eventtimer.EventTimer")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to project.version,
                "apiVersion" to apiVersion
            )
        }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    runServer {
        minecraftVersion("1.20.4")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}