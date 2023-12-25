FROM openjdk:17-alpine
RUN mkdir /opt/app
COPY /target/storyteller-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
ENTRYPOINT ["java", "-jar", "/opt/app/japp.jar"]

#FROM maven:3.8.3-openjdk-17 AS build
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/pom.xml clean package
#
#FROM openjdk:17-alpine
#COPY --from=build /usr/src/app/target/storyteller-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
#ENTRYPOINT ["java", "-jar", "/opt/app/japp.jar"]
