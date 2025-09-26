#FROM openjdk:latest
#COPY ./target/classes/com /tmp/com
#WORKDIR /tmp
#ENTRYPOINT ["java", "com.napier.devops.App"]


FROM openjdk:latest

WORKDIR /tmp

# Make sure to include the .jar extension
COPY ./target/Methods-0.2-alpha-2-jar-with-dependencies.jar /tmp

ENTRYPOINT ["java", "-jar", "Methods-0.2-alpha-2-jar-with-dependencies.jar"]
