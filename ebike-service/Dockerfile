FROM gradle:jdk21 as build
WORKDIR /workspace
COPY . /workspace/
RUN gradle clean bootJar

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 1926
CMD ["java", "-jar", "app.jar"]
