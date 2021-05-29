import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

group = "ru.revseev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
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
        attributes["Main-Class"] = "AppKt" // указать главный класс, метод main которого нужно запускать
    }
    duplicatesStrategy =
        DuplicatesStrategy.EXCLUDE // стратерия поведения при нахождении дубликатов библиотек в classpath

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    // то же будет сделано тут: https://github.com/wadejensen/kotlin-fat-jar-example
//    from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
//    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(uberJar)
    }
}
