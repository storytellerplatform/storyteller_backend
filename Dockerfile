FROM openjdk:17-alpine
RUN mkdir /opt/app
COPY /target/storyteller-0.0.1-SNAPSHOT.jar /opt/app/japp.jar
ENTRYPOINT ["java", "-jar", "/opt/app/japp.jar"]