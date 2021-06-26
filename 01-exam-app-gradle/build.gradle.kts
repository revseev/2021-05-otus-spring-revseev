import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
    kotlin("kapt") version "1.5.10"
}

group = "ru.revseev"
version = "1.2"
java.sourceCompatibility = JavaVersion.VERSION_11
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.shell:spring-shell-starter:2.0.1.RELEASE")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.8")
    implementation("net.jcip:jcip-annotations:1.0")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "junit-vintage-engine")
        exclude(module = "mockito-core")
        exclude(module = "assertj-core")
        exclude(module = "hamcrest")
    }
    testImplementation("io.strikt:strikt-core:0.31.0")
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.bootJar {
    manifest {
        attributes["Implementation-Title"] = rootProject.name
        attributes["Implementation-Version"] = archiveVersion
    }
}