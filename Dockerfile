FROM openjdk:8-jdk-alpine

RUN apk add --update curl
HEALTHCHECK --interval=10s --timeout=3s --start-period=60s \
        CMD curl -f http://localhost:8080/actuator/health || exit 1

ARG JAR_FILE
COPY ${JAR_FILE} app

EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]