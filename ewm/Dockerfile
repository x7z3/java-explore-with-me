FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY target/*.jar /app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]