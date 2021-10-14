plugins {
	application
	// html reporter for specs
	id("com.kncept.junit.reporter") version "2.1.0"
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("java")
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
}

application {
	mainClass.set("chess.App")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.hibernate:hibernate-core:5.5.7.Final")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation(group="io.springfox", name="springfox-swagger2", version ="2.7.0")
	implementation(group="io.springfox", name="springfox-swagger-ui", version="2.7.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	runtimeOnly("com.h2database:h2")
}

tasks.test {
	useJUnitPlatform()
}
