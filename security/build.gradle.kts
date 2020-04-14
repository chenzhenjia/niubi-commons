//apply(from = "../gradle/publish.gradle.kts")
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation Libs.jose4j
//    implementation Libs.javaJWT
//    implementation Libs.fastjson

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
val compileJava: JavaCompile by tasks
val processResources: ProcessResources by tasks

compileJava.dependsOn(processResources)
