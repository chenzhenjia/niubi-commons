plugins {
    `maven-publish`
    signing
    java
    `java-library`
    id("org.jetbrains.kotlin.jvm") version "1.3.70" apply false
    id("org.springframework.boot") version "2.2.1.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
}

allprojects {
    apply(plugin = "idea")
    configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
        module {
            outputDir = file("build/classes/main")
            testOutputDir = file("build/classes/test")
        }
    }
}

subprojects {
    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    java {
        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }
    }
    dependencies {
        "optionalImplementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        "optionalImplementation"("org.jetbrains.kotlin:kotlin-stdlib")
        "optionalImplementation"("org.jetbrains.kotlin:kotlin-reflect")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        compileOnly("org.springframework.boot:spring-boot-configuration-processor")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation("junit:junit:4.12")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
    tasks {
        withType<Javadoc>().configureEach {
            (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
            }
        }
        withType<JavaCompile> {
            dependsOn(processResources)
            options.isIncremental = true
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:unchecked")
            options.compilerArgs.add("-Xlint:deprecation")
        }
        withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
            enabled = false
        }
        withType<Jar> {
            enabled = true
        }
        withType<Javadoc> {
            isFailOnError = false
            options.encoding = "UTF-8"
        }
    }
//    afterEvaluate {
//        tasks.withType(PublishToMavenRepository) { task ->
//            if (task.publication.hasProperty('repo') && task.publication.repo != task.repository.name) {
//                task.enabled = false
//                task.group = null
//            }
//        }
//    }

    tasks.register<Jar>("sourcesJar") {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }
    tasks.register<Jar>("javadocJar") {
        val javadoc: Javadoc by tasks
        from(javadoc)
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                pom {
                    name.set("niubi-commons")
                    description.set("基于SpringMvc和Security权限管理")
                    url.set("https://github.com/chenzhenjia/niubi-commons")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("chzhenjia")
                            name.set("陈圳佳")
                            email.set("chzhenjia@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:https://github.com/chenzhenjia/niubi-commons.git")
                        developerConnection.set("scm:https://github.com/chenzhenjia/niubi-commons.git")
                        url.set("https://github.com/chenzhenjia/niubi-commons")
                    }
                }
                from(components["java"])
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])
            }
        }
        repositories {
            maven {
                credentials {
                    val mavenCentralUsername: String? by project
                    val mavenCentralPassword: String? by project
                    username = mavenCentralUsername
                    password = mavenCentralPassword
                }
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")
                url = if ("$version".endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            }
        }
    }
    signing {
        if (project.hasProperty("isCI")) {
            val signingKeyId: String? = properties["signing.keyId"]?.toString()
            val signingPassword: String? = properties["signing.password"]?.toString()
            val signingKey: String? by project
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        }
        sign(publishing.publications["mavenJava"])
    }
}