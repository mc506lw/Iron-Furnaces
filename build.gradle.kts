import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "2.3.0"
    idea
    id("com.gradleup.shadow") version "9.2.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

group = project.properties["group"]!!

repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://repo.xenondevs.xyz/releases") {
        name = "InvUI"
    }
}

val rebarVersion = project.properties["rebar.version"] as String
val minecraftVersion = project.properties["minecraft.version"] as String

dependencies {
    // Paper Dev Bundle (包含 API + NMS，26.1+ 不再混淆)
    paperweight.paperDevBundle("26.1.2.build.+")
    
    // 使用本地 Rebar jar（未发布到 Maven 仓库）
    compileOnly(files("libs/rebar-$rebarVersion.jar"))
    
    // InvUI (rebar 依赖)
    compileOnly("xyz.xenondevs.invui:invui:2.1.0")
    
    compileOnly(kotlin("stdlib"))
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_26
    targetCompatibility = JavaVersion.VERSION_26
}

kotlin {
    jvmToolchain(26)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
    
    // 统一 JVM target 为 25（兼容性最佳）
    options.release.set(25)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("25"))
    }
}

tasks.processResources {
    filteringCharset = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier = ""
}

bukkit {
    name = project.name
    main = project.properties["main-class"] as String
    version = project.version.toString()
    apiVersion = "26.1"
    depend = listOf("Rebar")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    
    authors = listOf("mc506lw")
}

// 26.1+ 使用 Mojang 映射（不再混淆）
paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

tasks.register<Copy>("copyToServer") {
    from(tasks.shadowJar)
    into("D:\\我的世界资源库\\服务器\\岚域3.0\\plugins")
    outputs.upToDateWhen { false }
}

tasks.build {
    finalizedBy("copyToServer")
}
