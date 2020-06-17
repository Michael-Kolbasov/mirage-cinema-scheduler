# temp container to build using gradle
FROM gradle:jdk11 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME/
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src

USER root
RUN chown -R gradle /home/gradle/src
RUN gradle build || return 0

COPY . .
RUN gradle clean build

# actual container
FROM openjdk:11
ARG BOT_USERNAME
ARG BOT_TOKEN
ENV ARTIFACT_NAME=mirage-scheduler.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

CMD java -DBOT.USERNAME=$BOT_USERNAME -DBOT.TOKEN=$BOT_TOKEN -jar $ARTIFACT_NAME