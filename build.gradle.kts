plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.3.9"
    kotlin("jvm") version "1.7.21"
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8")) // im not sure this is needed

    paperDevBundle("1.19.3-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
}

tasks.reobfJar {
    outputJar.set(file(rootProject.projectDir.absolutePath + "/build/SpaceGenerator.jar"))
}

tasks.build { dependsOn("reobfJar") }

tasks.compileKotlin {
    kotlinOptions { jvmTarget = "17" }
}