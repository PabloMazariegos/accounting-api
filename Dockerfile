FROM openjdk:11-slim
COPY build/libs/accounting-api.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

