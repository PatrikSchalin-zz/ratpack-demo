FROM openjdk:8-jre-alpine

MAINTAINER Patrik Schalin <patrik.schalin@systemverification.com>

EXPOSE 5050

COPY build/distributions/demo-app-shadow.zip .
RUN unzip demo-app-shadow.zip


ENTRYPOINT ["demo-app-shadow/bin/demo-app"]