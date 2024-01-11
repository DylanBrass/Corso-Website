FROM gradle:8.4 as builder
WORKDIR /usr/src/app
COPY src ./src
COPY build.gradle .
RUN ["gradle", "bootJar"]

EXPOSE 8080

FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /usr/src/app/${JAR_FILE} app.jar
COPY src/main/resources/gcp-credentials.json /app/gcp-credentials.json
ENTRYPOINT ["java", "-jar", "/app.jar"]
