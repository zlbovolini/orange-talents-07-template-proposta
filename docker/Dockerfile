# BUILD
FROM maven:3-jdk-11-slim as BUILD

MAINTAINER Lucas Bovolini lbovolini94@gmail.com

ENV APP_FOLDER=~/proposta

WORKDIR ${APP_FOLDER}

COPY src/ src/
COPY pom.xml .

RUN mvn -Dmaven.test.skip=true clean package
# Necessita BuildKit
# RUN --mount=type=cache,target=/root/.m2 mvn -Dmaven.test.skip=true clean package


# RUN
FROM openjdk:11-jre-slim

MAINTAINER Lucas Bovolini lbovolini94@gmail.com

ENV APP_NAME=proposta
ENV APP_FOLDER=~/proposta

RUN addgroup proposta && adduser --ingroup proposta proposta
USER proposta:proposta

WORKDIR ${APP_FOLDER}

COPY --from=BUILD "${APP_FOLDER}/target/${APP_NAME}*.jar" ${APP_NAME}.jar

EXPOSE 8080

ENTRYPOINT java -jar ${APP_NAME}.jar
