FROM amazoncorretto:17-alpine
MAINTAINER garvit-joshi
COPY target/mailing-service-1.0.jar mailing-service-1.0.jar
ENTRYPOINT ["java","-server","-Dspring.profiles.active=dev","-Xms1g","-Xmx4g","-XX:MaxMetaspaceSize=512m","-XX:+UseZGC","-jar","/mailing-service-1.0.jar"]
