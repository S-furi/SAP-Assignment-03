FROM gradle:8.6.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :service:buildFatJar --no-daemon --parallel

FROM openjdk:21
EXPOSE 11000
RUN mkdir /app
COPY --from=build /home/gradle/src/service/build/libs/service-all.jar /app/service-all.jar
ENTRYPOINT ["java","-jar","/app/service-all.jar"]

