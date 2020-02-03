# First stage: build jar in gradle jdk12 image
FROM gradle:jdk12 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

# Second stage: copy jar from first stage into light-weight alpine JRE image
FROM openjdk:12-alpine
EXPOSE 8080
COPY --from=builder /home/gradle/src/build/libs/ /app/
WORKDIR /app
ARG APP_VERSION
RUN mv /app/mars-attacks-${APP_VERSION}.jar /app/marsattacks.jar
ENTRYPOINT ["java","-jar","/app/marsattacks.jar"]
