FROM amazoncorretto:17-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
MAINTAINER garvit-joshi
COPY target/mailing-service-1.0.jar mailing-service-1.0.jar
ENTRYPOINT ["java","-server","-Dspring.profiles.active=dev","-Xms1g","-Xmx4g","-XX:MaxMetaspaceSize=512m","-XX:+UseZGC","-jar","/mailing-service-1.0.jar"]
