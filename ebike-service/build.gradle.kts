plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "it.unibo.sap.ass02"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.mariadb.jdbc:mariadb-java-client:3.1.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.73.Final:osx-aarch_64")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")
	implementation("org.springframework.boot:spring-boot-configuration-processor:3.3.4")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.4")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
