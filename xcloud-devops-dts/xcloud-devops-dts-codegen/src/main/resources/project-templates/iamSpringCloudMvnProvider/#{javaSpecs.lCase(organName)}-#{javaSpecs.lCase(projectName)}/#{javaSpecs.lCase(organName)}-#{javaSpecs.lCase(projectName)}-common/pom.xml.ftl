<!-- ${watermark} -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>${organName?uncap_first}-${projectName?uncap_first}-common</artifactId>
	<packaging>jar</packaging>
	<name>${organName?cap_first} ${projectName?cap_first} Common</name>
	<url>http://${projectName?uncap_first}.${organName?uncap_first}.${organType}</url>
	<parent>
		<groupId>${organType?uncap_first}.${organName?uncap_first}</groupId>
		<artifactId>${organName?uncap_first}-${projectName?uncap_first}</artifactId>
		<version>${version?uncap_first}</version>
	</parent>
	<properties>
		<java.version>1.8</java.version>
	</properties>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>com.wl4g</groupId>
				<artifactId>xcloud-bom</artifactId>
				<version>master</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-components-core</artifactId>
		</dependency>
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-iam-common</artifactId>
		</dependency>
		<dependency> <!-- https://github.com/springfox/springfox-demos -->
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>easyexcel</artifactId>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					${r'<source>${java.version}</source>'}
					${r'<target>${java.version}</target>'}
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<executions>
					<execution>
						<id>attach-sources</id>
						<phase>verify</phase>
						<goals>
							<goal>jar-no-fork</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-checkstyle-plugin</artifactId>
				<configuration>
					<configLocation>${basedir}/../checkstyle/google_checks.xml</configLocation>
				</configuration>
				<executions>
					<!-- Solutions to maven checkstyle errors in Eclipse: https://gitee.com/wl4g/xcloud-blogs/blob/master/articles/maven.checkstyle-eclipse-error/README_CN.md -->
					<!-- <execution> -->
					<!-- <id>checkstyle</id> -->
					<!-- <phase>validate</phase> -->
					<!-- <goals> -->
					<!-- <goal>check</goal> -->
					<!-- </goals> -->
					<!-- <configuration> -->
					<!-- <failOnViolation>true</failOnViolation> -->
					<!-- </configuration> -->
					<!-- </execution> -->
				</executions>
			</plugin>
		</plugins>
	</build>
</project>