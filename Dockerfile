FROM amazoncorretto:23.0.1-alpine
#FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine
ENV TZ=America/Asuncion
RUN apk add --no-cache tzdata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/atm-authorization-api-0.0.1-SNAPSHOT.jar atm-authorization-api.jar

ARG ENV=staging
ARG NEW_RELIC_LICENSE_KEY=3c6ec5e708e6093603ad7f46a05351f6FFFFNRAL

ENV NEW_RELIC_ENVIRONMENT=$ENV
ENV NEW_RELIC_LICENSE_KEY=$NEW_RELIC_LICENSE_KEY
ENV NEW_RELIC_AGENT_VERSION=8.6.0

# Setup newrelic
# TODO: add newrelic dependency to nexus and resolve it here
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/$NEW_RELIC_AGENT_VERSION/newrelic-agent-$NEW_RELIC_AGENT_VERSION.jar newrelic.jar
ADD newrelic.yml .

EXPOSE 8990
EXPOSE 10109
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/newrelic.jar","-jar","/atm-authorization-api.jar"]