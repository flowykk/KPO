plugins {
    kotlin("jvm") version "1.9.21"

}

group = "org.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.12.4")
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}