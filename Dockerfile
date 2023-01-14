# Build jar
# mvn clean install -Dmaven.test.skip=true

# Build image
# docker build -t ksionszka-api:latest .

# Run image
# docker run -p 8080:8080 --restart=on-failure ksionszka-api

FROM openjdk:17

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-noverify","-jar","/app.jar"]
