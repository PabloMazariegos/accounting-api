FROM openjdk:11-slim
COPY accounting-api.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

