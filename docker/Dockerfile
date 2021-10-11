# RUN
FROM openjdk:11-jre-slim

MAINTAINER Lucas Bovolini lbovolini94@gmail.com

ENV APP_NAME=proposta
ENV APP_FOLDER=~/proposta

RUN addgroup proposta && adduser --ingroup proposta proposta
USER proposta:proposta

WORKDIR ${APP_FOLDER}

COPY "./target/${APP_NAME}*.jar" ${APP_NAME}.jar

EXPOSE 8080

ENTRYPOINT java -jar ${APP_NAME}.jar
