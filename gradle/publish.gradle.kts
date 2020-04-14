/*
 * Copyright 2020 陈圳佳
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

apply(plugin = "maven-publish")
apply(plugin = "signing")
tasks.register<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}
tasks.register<Jar>("javadocJar") {
    val javadoc: Javadoc by tasks
    from(javadoc)
    archiveClassifier.set("javadoc")
}

extensions.configure<PublishingExtension> {
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
                username = "${properties["mavenCentralUsername"]}"
                password = "${properties["mavenCentralPassword"]}"
            }
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}
//val sign SigningExtension by configurations
extensions.configure<SigningExtension> {
    sign((extensions["publishing"] as PublishingExtension).publications["mavenJava"])
}