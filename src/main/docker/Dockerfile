FROM openjdk:11-slim

ARG BUILD_VERSION

ENV test="test"

COPY accounting-api-${BUILD_VERSION}.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

