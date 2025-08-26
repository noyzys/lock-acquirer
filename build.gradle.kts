plugins {
    id("java")
}

group = "dev.nautchkafe.studios.network.lock"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.velocitypowered.com/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}