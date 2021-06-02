import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.0"
}

group = "ru.revseev"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
}

val springVersion = "5.3.7"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework:spring-context:$springVersion")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("io.strikt:strikt-core:0.31.0")
    testImplementation("org.mockito:mockito-core:3.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val uberJar = tasks.register<Jar>("uberJar") {
    archiveClassifier.set("EXEC") // добавить постфикс к имени архива
    manifest {
        attributes["Main-Class"] =
            "ru.revseev.otus.spring.quizapp.QuizAppKt" // указать главный класс, метод main которого нужно запускать
    }
    duplicatesStrategy =
        DuplicatesStrategy.EXCLUDE // стратерия поведения при нахождении дубликатов библиотек в classpath

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks {
    "build" {
        dependsOn(uberJar)
    }
}
