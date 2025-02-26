FROM openjdk:17-alpine

COPY build/libs/*.jar app.jar

ENV PROFILE_NAME dev

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE_NAME}", "-jar", "app.jar"]