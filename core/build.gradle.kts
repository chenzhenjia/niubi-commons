//apply(from = "../gradle/publish.gradle.kts")
dependencies {
    optionalImplementation("org.slf4j:slf4j-api")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    optionalImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    optionalImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    optionalImplementation("org.springframework.boot:spring-boot-starter-data-redis")
}
