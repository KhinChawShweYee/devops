#FROM openjdk:eclipse-temurin:24
#COPY ./target/classes/com /tmp/com
#WORKDIR /tmp
#ENTRYPOINT ["java", "com.napier.devops.App"]

FROM eclipse-temurin:18
COPY ./target/devops.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops.jar", "db:3306", "30000"]

