plugins {
    `java-gradle-plugin`
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.spotless)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.guava)
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

spotless {
    encoding("UTF-8")
    java {
        importOrder()
        formatAnnotations()
        indentWithSpaces()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        googleJavaFormat()
    }
}

group = "com.s-kugel.schneider.generator"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("enumGenerator") {
            id = "com.s-kugel.schneider.plugin.enum-generator"
            implementationClass = "com.s_kugel.schneider.generator.EnumGenerator"
        }
        create("entityGenerator") {
            id = "com.s-kugel.schneider.plugin.entity-generator"
            implementationClass = "com.s_kugel.schneider.generator.EntityGenerator"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}
