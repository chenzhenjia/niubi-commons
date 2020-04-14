//apply(from = "../gradle/publish.gradle.kts")
dependencies {
    api(project(":core"))
    api("commons-collections:commons-collections:${properties["commonsCollectionsVersion"]}")

    optionalImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    optionalImplementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5")
    optionalImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    optionalImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    optionalImplementation("org.springframework.security:spring-security-config")
    optionalImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    optionalImplementation("org.springframework.boot:spring-boot-starter-web")
}