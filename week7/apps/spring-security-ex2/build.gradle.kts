plugins {
    id("java")
}

group = "com.villainscode"
version = "\"0.0.1-SNAPSHOT\""

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.4.5")
}

tasks.test {
    useJUnitPlatform()
}
