

plugins {
	java
	alias(libs.plugins.spring.plugin)
	alias(libs.plugins.spring.management)
	alias(libs.plugins.docker.compose)
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
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka:3.3.0")

}

tasks.withType<Test> {
	useJUnitPlatform()
}



tasks.register("composeDownaa") {
	group = "docker"
	description = "Stop Docker containers after testing"
	doLast {
		exec {
			commandLine("docker-compose", "-f", "docker-compose.test.yml", "down")
		}
	}
}


tasks.named("bootTestRun") {
	doFirst {
		dockerCompose {
			useComposeFiles = listOf("docker-compose.test.yml")
			checkContainersRunning = true
		}
	}
	finalizedBy("composeDown")
}
