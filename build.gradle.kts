import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.31"
}

group = "io.github.thefrontier"
version = "0.3.0"

repositories {
    mavenCentral()
    maven {
        setUrl("https://repo.spongepowered.org/maven")
    }
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.1.1")

    compileOnly("org.spongepowered:spongeapi:7.1.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}