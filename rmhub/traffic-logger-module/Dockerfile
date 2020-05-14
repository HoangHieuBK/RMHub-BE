FROM openjdk:11-jre-slim
MAINTAINER hino<ntquan@cmc.com.vn>

VOLUME /tmp
COPY target/*-exec.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java","-jar","/app.jar"]
