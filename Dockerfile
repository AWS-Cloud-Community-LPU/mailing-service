FROM amazoncorretto:17-alpine
MAINTAINER garvit-joshi
COPY target/mailing-service-1.0.jar mailing-service-1.0.jar
ENTRYPOINT ["java","-server","-Dspring.profiles.active=dev","-Xms512m","-Xmx1g","-XX:MaxMetaspaceSize=1g","-XX:+UseZGC","-jar","/mailing-service-1.0.jar"]