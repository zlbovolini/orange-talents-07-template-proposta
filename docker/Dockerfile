FROM openjdk:11-jre-slim

MAINTAINER Lucas Bovolini lbovolini94@gmail.com

ENV APP_NAME=proposta
ENV APP_FOLDER=/proposta

RUN addgroup proposta-dev && adduser --ingroup proposta-dev proposta-dev
USER proposta-dev:proposta-dev

WORKDIR ${APP_FOLDER}
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} ${APP_NAME}.jar

RUN ls -la ${APP_FOLDER}

ENTRYPOINT java -jar ${APP_FOLDER}/${APP_NAME}.jar

EXPOSE 8080