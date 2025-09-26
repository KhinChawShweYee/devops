#FROM openjdk:latest
#COPY ./target/classes/com /tmp/com
#WORKDIR /tmp
#ENTRYPOINT ["java", "com.napier.devops.App"]


FROM openjdk:latest
COPY ./target/Methods-0.1-alpha-2-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "Methods-0.1-alpha-2-jar-with-dependencies.jar"]