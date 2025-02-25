FROM openjdk:17-alpine

COPY build/libs/*.jar app.jar

ENV PROFILE_NAME raccoon

# 로컬 파일 시스템에서 Docker 컨테이너로 JAR 파일 복사
COPY sparta-project-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java", "-Dspring.profiles.active=${PROFILE_NAME}", "-jar", "app.jar"]