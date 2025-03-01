plugins {
    id("java")
    id("application")
    id("com.github.ben-manes.versions") version "0.42.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.4.0-jre")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("org.imdb.IMDB")
}
