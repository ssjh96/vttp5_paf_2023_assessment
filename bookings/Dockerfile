# MULTISTAGE
# Referenceing for first stage image (Build Stage)
# compiler & runtime names can be named anything, it is just for stage 2 to reference in the "--from=compiler".
# if compiler is not named, COPY --from=0 /code_folder/target/app.jar /app/app.jar
# --from=0 refers to the first unnamed stage (indexed as 0).
# FROM maven:3.9.9-eclipse-temurin-21 AS compiler

FROM maven:3.9.9-amazoncorretto-21 AS compiler

# These is just a name
ARG COMPILE_DIR=/code_folder
WORKDIR ${COMPILE_DIR}

# Copy project files and build
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn

#Compile and package the application
RUN mvn clean package -Dmaven.test.skip=true


# stage 2 (Runtime Stage)
# Consider using a lighter base image like  switch to a runtime-only image like eclipse-temurin:23-jre or openjdk:23-jre to reduce the image size. (use jre?)
# The maven image in the runtime stage includes unnecessary tools for running the application.
# FROM eclipse-temurin:23-jre AS runtime
# FROM maven:3.9.9-eclipse-temurin-23 AS runtime
# FROM maven:3.9.9-eclipse-temurin-21 AS runtime

FROM maven:3.9.9-amazoncorretto-21 AS runtime

ARG DEPLOY_DIR=/app
WORKDIR ${DEPLOY_DIR}

# Just copying the jar file and renaming it to day17l
COPY --from=compiler /code_folder/target/bookings-0.0.1-SNAPSHOT.jar bookings.jar

ENV SERVER_PORT=3000
EXPOSE ${SERVER_PORT}

# Delayed of 2 min before starting, health check every min
HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 \
   CMD curl http://localhost:${PORT}/status || exit 1

ENTRYPOINT ["java", "-jar", "bookings.jar"]