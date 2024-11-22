FROM gradle:8.6.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :app:buildFatJar --no-daemon --parallel

FROM openjdk:21
EXPOSE 4001
RUN mkdir /app
COPY --from=build /home/gradle/src/app/build/libs/app-all.jar /app/app-all.jar
ENTRYPOINT ["java","-jar","/app/app-all.jar"]


