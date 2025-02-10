# vttp5_paf_2023_assessment

use mybnb;

show tables;

select * from acc_occupancy;

describe reservations;
select * from reservations;

-- Check vacancy first
SELECT vacancy FROM acc_occupancy where acc_id = "13530122";
select * from acc_occupancy where acc_id = "13530122";

-- Insert the reservation
INSERT INTO reservations (resv_id, name, email, acc_id, arrival_date, duration) 
VALUES ('1A2B3C4D', 'Sheryl', 'sheryl@email.com', 13530122, '2025-09-20', 3);

-- Update the vacancy 
UPDATE acc_occupancy SET vacancy = 100 where acc_id = 13530122;
UPDATE acc_occupancy SET vacancy = 100 where acc_id = 30479760;

delete from reservations where name ='Sheryl';

SHOW VARIABLES LIKE 'read_only';

show grants for current_user();
SHOW VARIABLES LIKE 'read_only';

SELECT @@transaction_read_only;
SET SESSION transaction_read_only = ON;

made booking to 6228208
select * from acc_occupancy where acc_id = "6228208";
select * from reservations; // reference created was 83415130

mvn clean package
java -jar target/bookings-0.0.1-SNAPSHOT.jar

// DOCKER 1
FROM eclipse-temurin:23-noble AS compiler
# or eclipse-temurin:23-noble
# can be AS builder also

ARG COMPILE_DIR=/code_folder

WORKDIR ${COMPILE_DIR}

COPY src src
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

# Stage 2
FROM eclipse-temurin:23-jre-noble

ARG DEPLOY_DIR=/app

WORKDIR ${DEPLOY_DIR}

COPY --from=compiler /code_folder/target/bookings-0.0.1-SNAPSHOT.jar bookings.jar

ENV SERVER_PORT=3000

EXPOSE ${SERVER_PORT}

ENTRYPOINT [ "java", "-jar", "bookings.jar" ]



// DOCKER 2
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